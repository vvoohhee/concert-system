package io.hhplus.concert.domain.balance;

import java.util.Optional;

public interface BalanceRepository {
    Optional<Balance> findByUserId(Long id);
    Balance save(Balance balance);

    BalanceHistory saveHistory(BalanceHistory balanceHistory);
}
