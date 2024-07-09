package io.hhplus.concert.infrastructure.token;

import io.hhplus.concert.common.enums.TokenStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TokenJpaRepository extends JpaRepository<TokenEntity, Long> {
    TokenEntity findByToken(String token);
    TokenEntity save(TokenEntity token);
    @Query("SELECT t.id " +
            "FROM TokenEntity t " +
            "WHERE t.status = io.hhplus.concert.common.enums.TokenStatusType.WAITING " +
            "ORDER BY t.id DESC")
    Optional<Long> findFirstPositionIdOrderByIdDesc();
}
