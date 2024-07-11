package io.hhplus.concert.common.enums;

public enum TokenStatusType {
    WAITING(0),
    AVAILABLE(1),
    EXPIRED(2);

    private final int value;

    TokenStatusType(int value) {
        this.value = value;
    }
}
