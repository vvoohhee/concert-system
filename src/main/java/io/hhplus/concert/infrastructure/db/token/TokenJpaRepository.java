package io.hhplus.concert.infrastructure.db.token;

import io.hhplus.concert.common.enums.TokenStatusType;
import io.hhplus.concert.domain.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TokenJpaRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    List<Token> findByStatus(TokenStatusType status);

    List<Token> findByStatusAndExpireAtBefore(TokenStatusType status, LocalDateTime expireAt);

    List<Token> findByStatusAndAvailableAtBefore(TokenStatusType status, LocalDateTime expireAt);

    Token saveAndFlush(Token token);

    @Query("SELECT t.id " +
            "FROM Token t " +
            "WHERE t.status = io.hhplus.concert.common.enums.TokenStatusType.WAITING " +
            "ORDER BY t.id ASC")
    Optional<Long> findFirstPositionId();
}
