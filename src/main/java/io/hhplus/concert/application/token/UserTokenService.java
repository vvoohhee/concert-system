package io.hhplus.concert.application.token;

import io.hhplus.concert.domain.token.TokenInfo;

public interface UserTokenService {
    TokenInfo issueUserToken(Long userId);
    TokenInfo findUserToken(String authorization);
}
