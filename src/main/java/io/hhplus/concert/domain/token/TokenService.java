package io.hhplus.concert.domain.token;

import io.hhplus.concert.common.enums.TokenStatusType;
import io.hhplus.concert.common.exception.CustomException;
import io.hhplus.concert.common.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    /**
     * 토큰 발급 메서드
     *
     * @param userId 유저의 PK
     * @return Token (발급된 토큰)
     */
    @Transactional
    public Token issue(Long userId) {
        // 토큰의 기본 정보를 생성 후 DB에 저장 (처리시간, 만료시간 제외)
        Token token = new Token(userId);
        token = tokenRepository.save(token);

        // 현재 대기열 첫 순번인 토큰의 ID 조회
        Long firstPositionId = tokenRepository.findFirstPositionId().orElseThrow(() -> new NotFoundException("토큰 조회 실패"));

        // 생성된 사용자 토큰의 대기순번, 처리시간, 만료시간을 설정
        token.setPosition(token.getId(), firstPositionId);
        token.setAvailableAtAndExpireAt();

        return token;
    }

    /**
     * 토큰 조회 메서드
     *
     * @param tokenString 토큰 UUID
     * @return Token
     */
    public Token find(String tokenString) {
        Token token = tokenRepository.findByToken(tokenString).orElseThrow(() -> new NotFoundException("토큰 조회 실패"));

        if (token.getStatus().equals(TokenStatusType.EXPIRED)) throw new CustomException("만료된 토큰");

        Long first = tokenRepository.findFirstPositionId().orElseThrow(() -> new NotFoundException("토큰 조회 실패"));
        token.setPosition(token.getId(), first);

        return token;
    }

    /**
     * 토큰이 API 요청할 수 있는 (처리가능한) 상태인지 확인
     *
     * @param authorization 토큰
     * @return boolean 가능 여부
     */
    public boolean isAvailable(String authorization) {
        Token token = tokenRepository.findByToken(authorization).orElseThrow(() -> new NotFoundException("토큰 조회 실패"));

        return token.getStatus().equals(TokenStatusType.AVAILABLE)
                && token.getAvailableAt().isBefore(LocalDateTime.now())
                && token.getExpireAt().isAfter(LocalDateTime.now());
    }

    public void requestApi(String authorization) {
        Token token = tokenRepository.findByToken(authorization).orElseThrow(() -> new NotFoundException("토큰 조회 실패"));
        token.setLastRequestAt(LocalDateTime.now());
    }
}
