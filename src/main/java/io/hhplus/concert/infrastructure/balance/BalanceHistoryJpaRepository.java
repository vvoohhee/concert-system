package io.hhplus.concert.infrastructure.balance;

import io.hhplus.concert.domain.balance.BalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceHistoryJpaRepository extends JpaRepository<BalanceHistory, Long> {
}
