package io.hhplus.concert.infrastructure.concert;

import io.hhplus.concert.common.enums.ReservationStatusType;
import io.hhplus.concert.domain.concert.ConcertRepository;
import io.hhplus.concert.domain.concert.dto.SeatPriceInfo;
import io.hhplus.concert.domain.concert.model.ConcertOption;
import io.hhplus.concert.domain.concert.model.Reservation;
import io.hhplus.concert.domain.concert.model.Seat;
import io.hhplus.concert.infrastructure.concert.dto.SeatPriceProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertOptionJpaRepository concertOptionJpaRepository;
    private final SeatJpaRepository seatJpaRepository;
    private final ReservationJpaRepository reservationJpaRepository;

    @Override
    public Page<ConcertOption> findAvailableConcertOptions(Long reserveAt, Pageable pageable) {
        return concertOptionJpaRepository.findAvailableConcertOptions(reserveAt, pageable);
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
    public Optional<Seat> findSeatByIdWithPessimisticLock(Long id) {
        return seatJpaRepository.findSeatByIdWithPessimisticLock(id);
    }

    @Override
    public List<Seat> findSeatByIdIn(List<Long> ids) {
        return seatJpaRepository.findByIdIn(ids);
    }

    @Override
    public List<Seat> updateSeatStatusByIdIn(List<Long> ids, ReservationStatusType status) {
        return seatJpaRepository.updateStatusByIdIn(ids, status);
    }

    @Override
    public Reservation saveReservation(Reservation reservation) {
        return reservationJpaRepository.save(reservation);
    }

    @Override
    public List<Reservation> saveReservations(List<Reservation> reservations) {
        return reservationJpaRepository.saveAll(reservations);
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
    public List<SeatPriceInfo> findSeatPriceInfoBySeatIdIn(List<Long> seatIds) {
        return seatJpaRepository
                .findSeatsProjectionByIdIn(seatIds)
                .stream()
                .map(p -> new SeatPriceInfo(p.getSeatId(), p.getPrice()))
                .toList();
    }
}
