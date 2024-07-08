package io.hhplus.concert.common.enums;

public enum BalanceHistoryType {
    RECHARGE(0),
    CONSUME(1);

    private int type;

    BalanceHistoryType(int type) {
        this.type = type;
    }
}
