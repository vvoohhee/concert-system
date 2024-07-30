package io.hhplus.concert.domain.user.dto;

public record RechargeCommand(
        Long userId,
        Integer amount
) {
}
