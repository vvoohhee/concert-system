package io.hhplus.concert.domain.balance.command;

public record RechargeRequestCommand(
        Long userId,
        Integer amount
) {
}
