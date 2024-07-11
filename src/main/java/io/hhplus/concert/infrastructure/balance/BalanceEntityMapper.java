package io.hhplus.concert.infrastructure.balance;

import io.hhplus.concert.domain.balance.Balance;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BalanceEntityMapper {
    public Optional<Balance> toDomain(Optional<BalanceEntity> entity) {
        return entity.map(e ->
                new Balance(e.getId(),
                        e.getUserId(),
                        e.getBalance(),
                        e.getCreatedAt(),
                        e.getUpdatedAt())
        );
    }

    public Balance toDomain(BalanceEntity entity) {
        return new Balance(entity.getId(),
                entity.getUserId(),
                entity.getBalance(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public BalanceEntity toEntity(Balance domain) {
        return new BalanceEntity(domain.getId(),
                domain.getUserId(),
                domain.getBalance(),
                domain.getCreatedAt(),
                domain.getUpdatedAt());
    }
}
