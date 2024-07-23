package io.hhplus.concert.infrastructure.user;

import io.hhplus.concert.domain.user.BalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceHistoryJpaRepository extends JpaRepository<BalanceHistory, Long> {
}
