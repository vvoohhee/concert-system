package io.hhplus.concert.interfaces.presentation.balance.dto;

import io.hhplus.concert.domain.user.dto.BalanceInfo;
import lombok.Builder;

public class FindBalanceDto {
    @Builder
    public record Response(
            Long userId,
            int balance
    ) {
        public static FindBalanceDto.Response of(BalanceInfo balance) {
            return FindBalanceDto.Response.builder()
                    .balance(balance.balance())
                    .build();
        }
    }
}
