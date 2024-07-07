package io.hhplus.concert.presentation.balance.dto;

public class GetBalanceDto {
    public record Response(
            int balance
    ) {}
}
