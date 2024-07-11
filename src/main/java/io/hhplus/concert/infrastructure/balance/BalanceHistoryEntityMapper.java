package io.hhplus.concert.infrastructure.balance;

import io.hhplus.concert.domain.balance.BalanceHistory;
import org.springframework.stereotype.Component;

@Component
public class BalanceHistoryEntityMapper {
    BalanceHistoryEntity toEntity(BalanceHistory balanceHistory) {
        return BalanceHistoryEntity.builder()
                .id(balanceHistory.getId())
                .balanceId(balanceHistory.getBalanceId())
                .amount(balanceHistory.getAmount())
                .type(balanceHistory.getType())
                .createdAt(balanceHistory.getCreatedAt())
                .build();
    }

    public BalanceHistory toDomain(BalanceHistoryEntity entity) {
        return new BalanceHistory(entity.getBalanceId(), entity.getAmount(), entity.getType());
    }
}
