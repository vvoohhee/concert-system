package io.hhplus.concert.presentation.balance.dto;

public class RechargeBalanceDto {
    public record Request(
            Long userId,
            int amount
    ) {}

    public record Response(
            int status,
            String description,
            int balance
    ) {}


}
