package io.hhplus.concert.application.token;

import io.hhplus.concert.domain.token.TokenInfo;

public interface UserTokenService {
    TokenInfo issueWaitingToken(Long userId);
    TokenInfo findWaitingToken(String authorization);
    TokenInfo findActiveToken(String authorization);
    boolean isAvailableToken(String authorization);
    void activateToken();
    void expire(String authorization);
}
