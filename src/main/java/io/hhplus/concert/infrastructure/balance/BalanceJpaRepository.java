package io.hhplus.concert.infrastructure.balance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BalanceJpaRepository extends JpaRepository<BalanceEntity, Long> {
    Optional<BalanceEntity> findByUserId(Long id);
}
