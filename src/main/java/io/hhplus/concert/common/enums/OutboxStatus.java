package io.hhplus.concert.common.enums;

public enum OutboxStatus {
    INIT(0),
    PUBLISHED(1);

    private int value;

    OutboxStatus(int value) {
        this.value = value;
    }
}
