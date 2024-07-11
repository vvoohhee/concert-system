package io.hhplus.concert.infrastructure.balance;

import io.hhplus.concert.domain.balance.Balance;
import io.hhplus.concert.domain.balance.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class BalanceRepositoryImpl implements BalanceRepository {
    private final BalanceEntityMapper mapper;
    private final BalanceJpaRepository repository;

    @Override
    public Optional<Balance> findByUserId(Long id) {
        return mapper.toDomain(repository.findByUserId(id));
    }

    @Override
    public Balance save(Balance balance) {
        return mapper.toDomain(repository.save(mapper.toEntity(balance)));
    }
}
