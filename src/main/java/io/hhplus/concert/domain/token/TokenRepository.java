package io.hhplus.concert.domain.token;

import io.hhplus.concert.common.enums.TokenStatusType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TokenRepository {
    Boolean issueWaitingToken(Token token);
    void issueActiveTokens(List<Token> tokens);
    Long findRank(String token);
    Optional<Token> findWaitingTokenByToken(String token);
    Optional<Token> findActiveTokenByToken(String token);
    Integer findActiveTokenCount();
    List<Token> findByStatusAndExpireAtBefore(TokenStatusType status, LocalDateTime expireAt);
    Optional<Long> findFirstPositionId();
    List<String> findAvailableWaitingTokens(int count);
    void deleteActivatedWaitingToken(int count);
    Boolean deleteActiveToken(String token);
    Boolean deleteTokenUserData(String token);
}
