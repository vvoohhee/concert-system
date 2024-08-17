package io.hhplus.concert.infrastructure.db.user;

import io.hhplus.concert.domain.user.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BalanceJpaRepository extends JpaRepository<Balance, Long> {
    Optional<Balance> findByUserId(Long id);
}
