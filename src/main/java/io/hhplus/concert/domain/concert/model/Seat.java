package io.hhplus.concert.domain.concert.model;

import io.hhplus.concert.common.enums.ErrorCode;
import io.hhplus.concert.common.enums.ReservationStatusType;
import io.hhplus.concert.common.exception.CustomException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "seat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
        if (Objects.isNull(concertOptionId)) throw new CustomException(ErrorCode.ILLEGAL_ARGUMENT);
        if (number > ConcertOption.DEFAULT_SEAT_QUANTITY || number < 0)
            throw new CustomException(ErrorCode.ILLEGAL_ARGUMENT);
        status = ReservationStatusType.AVAILABLE;
    }
}
