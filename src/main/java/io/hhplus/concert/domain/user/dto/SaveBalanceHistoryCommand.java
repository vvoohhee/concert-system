package io.hhplus.concert.domain.user.dto;

import io.hhplus.concert.common.enums.BalanceHistoryType;

public record SaveBalanceHistoryCommand(
        Long balanceId,
        Integer amount,
        BalanceHistoryType type
) {
}
