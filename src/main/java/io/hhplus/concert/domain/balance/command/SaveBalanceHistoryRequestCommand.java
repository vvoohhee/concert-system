package io.hhplus.concert.domain.balance.command;

import io.hhplus.concert.common.enums.BalanceHistoryType;

public record SaveBalanceHistoryRequestCommand (
        Long balanceId,
        Integer amount,
        BalanceHistoryType type
) {
}
