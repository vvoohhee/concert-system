package io.hhplus.concert.domain.token;

import io.hhplus.concert.common.enums.ErrorCode;
import io.hhplus.concert.common.enums.TokenStatusType;
import io.hhplus.concert.common.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    /**
     * 대기열 토큰 발급 메서드
     *
     * @param userId 유저의 PK
     * @return Token (발급된 토큰)
     */
    @Transactional
    public Token issueWaitingToken(Long userId) {
        // 토큰의 기본 정보를 생성 후 DB에 저장 (처리시간, 만료시간 제외)
        Token token = new Token(userId);
        Boolean issueResult = tokenRepository.issueWaitingToken(token);
        if (issueResult == null || !issueResult) throw new CustomException(ErrorCode.REDIS_ERROR);

        return token;
    }

    /**
     * 대기열 토큰 조회 메서드
     *
     * @param tokenString 토큰 UUID
     * @return Token
     */
    public Token findWaitingToken(String tokenString) {
        Long rank = tokenRepository.findRank(tokenString);

        // Rank 조회에는 문제가 없지만, 토큰 값과 일치하는 데이터가 없어 NULL이 내려올 때 예외처리
        if (rank == null) throw new CustomException(ErrorCode.TOKEN_NOT_EXIST);

        Token token = new Token();
        token.setToken(tokenString);
        token.setStatus(TokenStatusType.WAITING);
        token.setPosition(++rank);

        return token;
    }

    /**
     * 토큰으로 ACTIVE 토큰 정보 조회
     *
     * @param authorization 토큰
     * @return Token 객체
     */
    public Token findActiveToken(String authorization) {
        return tokenRepository
                .findActiveTokenByToken(authorization)
                .orElseThrow(() -> new CustomException(ErrorCode.TOKEN_NOT_EXIST));
    }

    /**
     * 토큰이 API를 요청할 수 있는 (처리가능한) 상태인지 확인하기 위해
     * ACTIVE 토큰 중에 요청한 토큰과 일치하는 데이터가 있는지 확인
     *
     * @param authorization 토큰
     * @return boolean 가능 여부
     */
    public boolean isAvailable(String authorization) {
        return tokenRepository
                .findActiveTokenByToken(authorization)
                .isPresent();
    }

    public void updateLastRequestTime(String authorization) {
        Token token = tokenRepository.findWaitingTokenByToken(authorization).orElseThrow(() -> new CustomException(ErrorCode.TOKEN_NOT_EXIST));
        token.updateLastRequestAt(LocalDateTime.now());
    }

    /**
     * 놀이공원 방식으로 대기열 토큰을 관리
     * ACTIVE로 변경될 수 있는 토큰을 조회해 ACTIVE 토큰을 생성하고, WAITING 토큰은 대기열에서 삭제 처리
     */
    public void activateToken() {
        List<String> waitingTokens = tokenRepository.findAvailableWaitingTokens(Token.ACTIVATABLE_COUNT_PER_MIN);
        Integer activeCount = tokenRepository.findActiveTokenCount();

        if (activeCount > Token.MAX_ACTIVE_LIMIT) return;

        // 토큰 값으로 토큰을 소유한 회원 정보 조회
        List<Token> tokens = new ArrayList<>();
        for (String tokenString : waitingTokens) {
            tokenRepository.findWaitingTokenByToken(tokenString).ifPresent(tokens::add);
        }

        tokenRepository.issueActiveTokens(tokens);
        tokenRepository.deleteActivatedWaitingToken(tokens.size());
    }

    /**
     * 결제 완료 시에 토큰을 만료
     */
    public void deleteActiveToken(String authorization) {
        Boolean tokenDeleteResult = tokenRepository.deleteActiveToken(authorization);

        if (tokenDeleteResult) log.info("[결제API][토큰: {}] 레디스에서 토큰 삭제 성공", authorization);
        else log.info("[결제API][토큰: {}] 레디스에서 토큰 삭제 실패", authorization);

        Boolean tokenUserDataDeleteResult = tokenRepository.deleteTokenUserData(authorization);

        if (tokenUserDataDeleteResult) log.info("[결제API][토큰: {}] 레디스에서 토큰-유저 매핑 데이터 삭제 성공", authorization);
        else log.info("[결제API][토큰: {}] 레디스에서 토큰-유저 매핑 데이터 삭제 실패", authorization);
    }
}
