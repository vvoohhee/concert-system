package io.hhplus.concert.domain.concert;

import io.hhplus.concert.common.enums.ReservationStatusType;
import io.hhplus.concert.domain.concert.model.ConcertOption;
import io.hhplus.concert.domain.concert.model.Reservation;
import io.hhplus.concert.domain.concert.model.Seat;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertRepository {
    List<ConcertOption> findAvailableConcertOptions(LocalDateTime reserveAt);

    List<Seat> findSeatByConcertOptionId(Long concertOptionId);

    List<Seat> findSeatByStatus(ReservationStatusType status);

    List<Seat> findSeatByIdIn(List<Long> ids);

    Reservation saveReservation(Reservation reservation);

    List<Reservation> findReservationBySeatId(Long seatId);

    List<Reservation> findReservationByReservedBy(Long userId);

    void deleteReservationById(Long id);

    ConcertOption findConcertOptionBySeatId(Long seatId);
}
