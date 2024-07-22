package io.hhplus.concert.infrastructure.balance;

import io.hhplus.concert.domain.balance.Balance;
import io.hhplus.concert.domain.balance.BalanceHistory;
import io.hhplus.concert.domain.balance.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class BalanceRepositoryImpl implements BalanceRepository {
    private final BalanceJpaRepository repository;
    private final BalanceHistoryJpaRepository historyRepository;

    @Override
    public Optional<Balance> findByUserId(Long id) {
        return repository.findByUserId(id);
    }

    @Override
    public Balance save(Balance balance) {
        return repository.save(balance);
    }

    @Override
    public BalanceHistory saveHistory(BalanceHistory balanceHistory) {
        return historyRepository.save(balanceHistory);
    }
}
