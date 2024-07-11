package io.hhplus.concert.presentation.balance.dto;

import io.hhplus.concert.domain.balance.command.RechargeRequestCommand;
import io.hhplus.concert.domain.balance.command.RechargeResponseCommand;

public class RechargeBalanceDto {
    public record Request(
            Long userId,
            Integer amount
    ) {}

    public record Response(
            int balance
    ) {}

    public static RechargeRequestCommand toCommand(RechargeBalanceDto.Request request) {
        return new RechargeRequestCommand(request.userId, request.amount);
    }

    public static RechargeBalanceDto.Response of(RechargeResponseCommand command) {
        return new RechargeBalanceDto.Response(command.balance());
    }

}
