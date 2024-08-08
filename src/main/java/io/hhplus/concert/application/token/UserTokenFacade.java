package io.hhplus.concert.application.token;

import io.hhplus.concert.common.enums.ErrorCode;
import io.hhplus.concert.common.enums.TokenStatusType;
import io.hhplus.concert.common.exception.CustomException;
import io.hhplus.concert.domain.token.Token;
import io.hhplus.concert.domain.token.TokenInfo;
import io.hhplus.concert.domain.token.TokenService;
import io.hhplus.concert.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserTokenFacade implements UserTokenService {
    private final TokenService tokenService;
    private final UserService userService;

    @Override
    public TokenInfo issueWaitingToken(Long userId) {
        // 토큰 생성 전에 유효한 유저의 요청인지 확인
        if(userService.findUser(userId).isEmpty()) throw new CustomException(ErrorCode.NO_DATA);

        Token token = tokenService.issueWaitingToken(userId);
        token = tokenService.findWaitingToken(token.getToken());
        return new TokenInfo(token.getToken(), token.getStatus(), token.getPosition());
    }

    @Override
    public TokenInfo findWaitingToken(String tokenString) {
        Token token = tokenService.findWaitingToken(tokenString);
        return new TokenInfo(token.getToken(), token.getStatus(), token.getPosition());
    }

    @Override
    public TokenInfo findActiveToken(String authorization) {
        Token token = tokenService.findActiveToken(authorization);
        token.setStatus(TokenStatusType.AVAILABLE);
        return new TokenInfo(token.getToken(), token.getStatus(), token.getPosition());
    }

    @Override
    public boolean isAvailableToken(String authorization) {
        return tokenService.isAvailable(authorization);
    }

    @Override
    public void activateToken() {
        tokenService.activateToken();
    }

    @Override
    public void expire(String authorization) {
        tokenService.deleteActiveToken(authorization);
    }
}
