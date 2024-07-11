package io.hhplus.concert.domain.concert.dto;

import io.hhplus.concert.common.enums.ReservationStatusType;

public record SeatInfo(
        Long id,
        int number,
        ReservationStatusType status
) {
}
