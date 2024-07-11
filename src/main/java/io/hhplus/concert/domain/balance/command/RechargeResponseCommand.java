package io.hhplus.concert.domain.balance.command;

public record RechargeResponseCommand(
        Long id,
        int balance
) {
}
