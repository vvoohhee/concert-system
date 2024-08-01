package io.hhplus.concert.infrastructure.token;

import io.hhplus.concert.common.enums.ErrorCode;
import io.hhplus.concert.common.exception.CustomException;
import io.hhplus.concert.domain.token.Token;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public class TokenRedisRepository {

    private final static String WAITING_QUEUE = "WAITING-QUEUE";
    private final static String ACTIVE_KEY_PREFIX = "ACTIVE:";

    private final RedisTemplate<String, String> redisTemplate;
    private ZSetOperations<String, String> zSetOperations;
    private ValueOperations<String, String> valueOperations;

    @PostConstruct
    private void init() {
        zSetOperations = redisTemplate.opsForZSet();
        valueOperations = redisTemplate.opsForValue();
    }

    /**
     * 대기열에 토큰 저장, token:userId를 key:value로 가지는 redis string 생성
     *
     * @param token
     * @return Token
     */
    public Boolean issueWaitingToken(Token token) {
        long timestamp = System.currentTimeMillis();

        Boolean result = null;
        try {
            // 대기열에 토큰 추가, token:userId로 redis string 추가
            result = zSetOperations.add(WAITING_QUEUE, token.getToken(), timestamp);
            valueOperations.set(token.getToken(), token.getUserId().toString(), 6, TimeUnit.HOURS);
            log.info("[토큰 생성][유저 ID : {}] 토큰 생성 요청 - member : {}", token.getUserId(), token.getToken());
        } catch (Exception e) {
            log.error("[토큰 생성][유저 ID : {}] 토큰 생성 재시도 요청 중 에러 발생", token.getUserId(), e);

            waitBeforeRetry();
            try {
                result = zSetOperations.add(WAITING_QUEUE, token.getToken(), timestamp);
            } catch (Exception e2) {
                log.error("[토큰 생성][유저 ID : {}] 토큰 생성 재시도 요청 중 에러 발생", token.getUserId(), e);
                throw new CustomException(ErrorCode.REDIS_ERROR);
            }
        }

        return result;
    }

    /**
     * 대기열 토큰의 현재 순위 조회
     *
     * @param token
     * @return 대기열 토큰의 현재 순위
     */
    public Long findRank(String token) {
        Long rank = null;
        try {
            rank = zSetOperations.rank(WAITING_QUEUE, token);
            log.info("[토큰 순위 조회][토큰 : {}] 순위 : {}", token, rank);
        } catch (Exception e) {
            waitBeforeRetry();
            rank = zSetOperations.rank(WAITING_QUEUE, token);
        }

        return rank;
    }

    /**
     * ACTIVE로 변경할 수 있는 대기열 토큰 리스트 조회
     *
     * @param count 최대 변경 가능 개수
     * @return List<String> 토큰 리스트
     */
    public List<String> findAvailableWaitingTokens(int count) {
        Set<String> range = null;
        try {
            range = zSetOperations.range(WAITING_QUEUE, 0, count - 1);
        } catch (Exception e) {
            waitBeforeRetry();
            range = zSetOperations.range(WAITING_QUEUE, 0, count - 1);
        }

        if (range == null) return List.of();

        return range.stream().toList();
    }

    /**
     * 대기열 토큰으로 토큰과 일치하는 토큰+유저 정보 조회
     *
     * @param tokenString
     * @return Optional<Token>
     */
    public Optional<Token> findWaitingTokenByToken(String tokenString) {
        // 대기열에 토큰이 존재하는지 확인
        Double score = null;
        try {
            score = zSetOperations.score(WAITING_QUEUE, tokenString);
        } catch (Exception e) {
            waitBeforeRetry();
            score = zSetOperations.score(WAITING_QUEUE, tokenString);
        }

        // 대기열에 토큰이 없으면 return Optional.empty()
        if (score == null) return Optional.empty();

        // 토큰과 일치하는 유저 정보 조회
        String value = null;
        try {
            value = valueOperations.get(tokenString);
            log.info("[토큰 활성화][토큰 : {}] 유저 ID : {}", tokenString, value);
        } catch (Exception e) {
            waitBeforeRetry();
            value = valueOperations.get(tokenString);
        }

        // 토큰을 key로 userId를 조회한 결과가 없으면 return Optional.empty()
        if (value == null) return Optional.empty();

        return Optional.of(new Token(tokenString, Long.parseLong(value)));
    }

    /**
     * ACTIVE 토큰 발급
     * ACTIVE 토큰의 형식 : key:value = "ACTIVE:{token}":"{userId}"
     *
     * @param tokens
     */
    public void issueActiveTokens(List<Token> tokens) {
        redisTemplate.executePipelined((RedisCallback<StringRedisConnection>) connection -> {
            for (Token token : tokens) {
                String key = ACTIVE_KEY_PREFIX + token.getToken();
                String value = token.getUserId().toString();

                connection.stringCommands().set(
                        redisTemplate.getStringSerializer().serialize(key),
                        redisTemplate.getStringSerializer().serialize(value),
                        Expiration.from(Token.TTL_MIN, TimeUnit.MINUTES),
                        RedisStringCommands.SetOption.SET_IF_ABSENT
                );
            }
            return null;
        });

        log.info("[ACTIVE 토큰 생성] WAITING 토큰 {}개에 대하여 ACTIVE 토큰 생성 요청", tokens.size());

    }

    /**
     * ACTIVE 토큰으로 토큰 정보를 조회
     * ACTIVE 토큰의 형식 : key:value = "ACTIVE:{token}":"{userId}"
     *
     * @param token ACTIVE 토큰
     * @return Token 객체
     */
    public Optional<Token> findActiveTokenByToken(String token) {
        String value = null;
        try {
            value = valueOperations.get(ACTIVE_KEY_PREFIX + token);
        } catch (Exception e) { // 레디스 문제로 실패하는 경우 재시도 최대 3번
            for (int i = 0; i < 3; i++) {
                value = valueOperations.get(ACTIVE_KEY_PREFIX + token);
                if (value != null) return Optional.of(new Token(token, Long.parseLong(value)));
            }
            return Optional.empty();
        }

        if (Objects.isNull(value)) return Optional.empty();
        return Optional.of(new Token(token, Long.parseLong(value)));
    }

    /**
     * 현재 ACTIVE 토큰의 개수를 조회
     *
     * @return Integer (ACTIVE 토큰의 개수)
     */
    public Integer findActiveTokenCount() {
        Set<String> keys;
        try {
            keys = redisTemplate.keys(ACTIVE_KEY_PREFIX + "*");
        } catch (Exception e) {
            waitBeforeRetry();
            keys = redisTemplate.keys(ACTIVE_KEY_PREFIX + "*");
        }

        return keys == null ? 0 : keys.size();
    }

    /**
     * 대기열 토큰 삭제
     * Rank를 0부터 count개만큼 삭제
     *
     * @param count 삭제할 개수
     */
    public void deleteWaitingToken(int count) {
        List<Object> result = redisTemplate.executePipelined((RedisCallback<?>) connection ->
                connection.zSetCommands().zRemRange(WAITING_QUEUE.getBytes(), 0, count - 1)
        );

        log.info("[토큰 삭제] ACTIVE로 전환된 WAITING 토큰 {}개 삭제 요청", result.get(0).toString());
    }

    /**
     * Retry를 위한 메서드
     * Redis에서 Exception이 발생하면 100ms 후에 재요청을 할 수 있도록 기다린다.
     */
    private void waitBeforeRetry() {
        try {
            Thread.sleep(100); // 100ms 대기
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
