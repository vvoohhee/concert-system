package io.hhplus.concert.domain.concert;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Reservation {
    private Long id;
    private Long seatId;
    private Long reservedBy;
    private LocalDateTime reservedAt;

    public final static Integer LIFETIME = 5; // minute

    public Reservation(Long seatId, Long reservedBy) {
        this.seatId = seatId;
        this.reservedBy = reservedBy;
        this.reservedAt = LocalDateTime.now();
    }
}
