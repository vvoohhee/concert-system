package io.hhplus.concert.domain.balance.dto;

public record RechargeCommand(
        Long userId,
        Integer amount
) {
}
