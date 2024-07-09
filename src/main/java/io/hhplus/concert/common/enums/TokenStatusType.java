package io.hhplus.concert.common.enums;

public enum TokenStatusType {
    WAITING(0),
    PENDING(1),
    DONE(2),
    EXPIRED(3);

    private final int value;

    TokenStatusType(int value) {
        this.value = value;
    }
}
