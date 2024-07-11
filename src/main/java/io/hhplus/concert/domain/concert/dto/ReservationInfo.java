package io.hhplus.concert.domain.concert.dto;

import java.time.LocalDateTime;

public record ReservationInfo(
        Long seatId,
        Long reservedBy,
        LocalDateTime reservedAt
) {
}
