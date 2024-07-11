package io.hhplus.concert.domain.concert.model;

import io.hhplus.concert.common.enums.ReservationStatusType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="seat_id", nullable = false)
    private Long seatId;

    @Column(name="reserved_by", nullable = false)
    private Long reservedBy;

    @Column(name="reserved_at", nullable = false)
    private LocalDateTime reservedAt;

    public final static Integer LIFETIME = 5; // minute

    public Reservation(Long seatId, Long reservedBy, LocalDateTime reservedAt) {
        this.seatId = seatId;
        this.reservedBy = reservedBy;
        this.reservedAt = reservedAt;
    }
}
