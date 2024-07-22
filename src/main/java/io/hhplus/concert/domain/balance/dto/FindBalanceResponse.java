package io.hhplus.concert.domain.balance.dto;

import java.time.LocalDateTime;

public record FindBalanceResponse(
        int balance,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
