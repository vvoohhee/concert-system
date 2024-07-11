package io.hhplus.concert.domain.concert.model;

import io.hhplus.concert.common.enums.ReservationStatusType;
import io.hhplus.concert.common.exception.CustomException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "seat")
@Getter
@Setter
@NoArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "concert_option_id", nullable = false)
    private Long concertOptionId;

    @Column(name = "number")
    private Integer number;

    @Column(name = "status", nullable = false)
    private ReservationStatusType status;

    public Seat(Long concertOptionId, Integer number) {
        if (Objects.isNull(concertOptionId)) throw new IllegalArgumentException("유효하지 않은 콘서트 옵션 ID");
        if (number > ConcertOption.DEFAULT_SEAT_QUANTITY || number < 0)
            throw new IllegalArgumentException("유효하지 않은 좌석 번호");
        status = ReservationStatusType.AVAILABLE;
    }
}
