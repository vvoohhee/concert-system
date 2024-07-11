package io.hhplus.concert.application.token;

import io.hhplus.concert.domain.token.Token;
import io.hhplus.concert.domain.token.TokenInfo;
import io.hhplus.concert.domain.token.TokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserTokenFacade implements UserTokenService {
    private final TokenService tokenService;

    @Override
    @Transactional
    public TokenInfo issueUserToken(Long userId) {
        Token token = tokenService.issue(userId);
        return new TokenInfo(token.getToken(), token.getStatus(), token.getPosition());
    }

    @Override
    @Transactional
    public TokenInfo findUserToken(String tokenString) {
        Token token = tokenService.find(tokenString);
        return new TokenInfo(token.getToken(), token.getStatus(), token.getPosition());
    }

    @Override
    @Transactional
    public boolean isAvailableToken(String authorization) {
        boolean isAvailable = tokenService.isAvailable(authorization);

        if (isAvailable) {
            tokenService.requestApi(authorization);
        }

        return isAvailable;
    }
}
