package io.hhplus.concert.domain.token;

import io.hhplus.concert.common.enums.TokenStatusType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TokenRepository {
    List<Token> findAll();
    Optional<Token> findByToken(String token);
    List<Token> findByStatus(TokenStatusType status);
    List<Token> findByStatusAndExpireAtBefore(TokenStatusType status, LocalDateTime expireAt);
    List<Token> findByStatusAndAvailableAtBefore(TokenStatusType status, LocalDateTime availableAt);
    Optional<Long> findFirstPositionId();
    Token save(Token token);
    List<Token> saveAll(List<Token> tokens);
}
