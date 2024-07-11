package io.hhplus.concert.infrastructure.balance;

import io.hhplus.concert.domain.balance.BalanceHistory;
import io.hhplus.concert.domain.balance.BalanceHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceHistoryRepositoryImpl implements BalanceHistoryRepository {

    private final BalanceHistoryEntityMapper mapper;
    private final BalanceHistoryJpaRepository repository;

    @Override
    public BalanceHistory save(BalanceHistory balanceHistory) {
        return mapper.toDomain(
                repository.save(mapper.toEntity(balanceHistory))
        );
    }
}
