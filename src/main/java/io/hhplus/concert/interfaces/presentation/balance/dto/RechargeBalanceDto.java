package io.hhplus.concert.interfaces.presentation.balance.dto;

import io.hhplus.concert.domain.user.dto.BalanceInfo;

public class RechargeBalanceDto {
    public record Request(
            Long userId,
            Integer amount
    ) {
    }

    public record Response(
            int balance
    ) {
        public static RechargeBalanceDto.Response of(BalanceInfo balanceInfo) {
            return new RechargeBalanceDto.Response(balanceInfo.balance());
        }
    }
}
