package io.hhplus.concert.domain.concert;

import io.hhplus.concert.common.enums.ReservationStatusType;
import io.hhplus.concert.domain.concert.dto.SeatPriceInfo;
import io.hhplus.concert.domain.concert.model.ConcertOption;
import io.hhplus.concert.domain.concert.model.Reservation;
import io.hhplus.concert.domain.concert.model.Seat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConcertRepository {
    Page<ConcertOption> findAvailableConcertOptions(Long reserveAt, Pageable pageable);

    List<Seat> findSeatByConcertOptionId(Long concertOptionId);

    List<Seat> findSeatByStatus(ReservationStatusType status);

    Optional<Seat> findSeatByIdWithPessimisticLock(Long id);

    List<Seat> findSeatByIdIn(List<Long> ids);

    List<Seat> updateSeatStatusByIdIn(List<Long> ids, ReservationStatusType status);

    Reservation saveReservation(Reservation reservation);

    List<Reservation> saveReservations(List<Reservation> reservations);

    List<Reservation> findReservationBySeatId(Long seatId);

    List<Reservation> findReservationByReservedBy(Long userId);

    void deleteReservationById(Long id);

    List<SeatPriceInfo> findSeatPriceInfoBySeatIdIn(List<Long> seatIds);
}
