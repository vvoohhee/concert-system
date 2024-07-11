package io.hhplus.concert.infrastructure.concert;

import io.hhplus.concert.common.enums.ReservationStatusType;
import io.hhplus.concert.domain.concert.ConcertRepository;
import io.hhplus.concert.domain.concert.model.ConcertOption;
import io.hhplus.concert.domain.concert.model.Reservation;
import io.hhplus.concert.domain.concert.model.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertOptionJpaRepository concertOptionJpaRepository;
    private final SeatJpaRepository seatJpaRepository;
    private final ReservationJpaRepository reservationJpaRepository;

    @Override
    public List<ConcertOption> findAvailableConcertOptions(LocalDateTime reserveAt) {
        return concertOptionJpaRepository.findAvailableConcertOptions(reserveAt);
    }

    @Override
    public List<Seat> findSeatByConcertOptionId(Long concertOptionId) {
        return seatJpaRepository.findByConcertOptionId(concertOptionId);
    }

    @Override
    public List<Seat> findSeatByStatus(ReservationStatusType status) {
        return seatJpaRepository.findByStatus(status);
    }

    @Override
    public List<Seat> findSeatByIdIn(List<Long> ids) {
        return seatJpaRepository.findByIdIn(ids);
    }

    @Override
    public Reservation saveReservation(Reservation reservation) {
        return reservationJpaRepository.save(reservation);
    }

    @Override
    public List<Reservation> findReservationBySeatId(Long seatId) {
        return reservationJpaRepository.findBySeatId(seatId);
    }

    @Override
    public List<Reservation> findReservationByReservedBy(Long userId) {
        return reservationJpaRepository.findByReservedBy(userId);
    }

    @Override
    public void deleteReservationById(Long id) {
        reservationJpaRepository.deleteById(id);
    }

    @Override
    public ConcertOption findConcertOptionBySeatId(Long seatId) {
        return concertOptionJpaRepository.findTopBySeatId(seatId);
    }
}
