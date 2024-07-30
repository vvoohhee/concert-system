package io.hhplus.concert.domain.user.dto;

import java.time.LocalDateTime;

public record FindBalanceResponse(
        int balance,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
