package io.hhplus.concert.application.concert;

import io.hhplus.concert.domain.concert.ConcertService;
import io.hhplus.concert.domain.concert.dto.ConcertOptionInfo;
import io.hhplus.concert.domain.concert.dto.ReservationInfo;
import io.hhplus.concert.domain.concert.dto.SeatInfo;
import io.hhplus.concert.domain.concert.model.ConcertOption;
import io.hhplus.concert.domain.concert.model.Reservation;
import io.hhplus.concert.domain.concert.model.Seat;
import io.hhplus.concert.domain.token.Token;
import io.hhplus.concert.domain.token.TokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserConcertFacade implements UserConcertService {
    private final TokenService tokenService;
    private final ConcertService concertService;

    @Override
    public List<ConcertOptionInfo> findConcerts(LocalDateTime reserveAt) {
        return concertService.findConcerts(reserveAt);
    }

    @Override
    public List<SeatInfo> findSeats(Long concertOptionId) {
        List<Seat> seats = concertService.findSeats(concertOptionId);
        return seats.stream()
                .map(seat -> new SeatInfo(seat.getId(), seat.getNumber(), seat.getStatus()))
                .toList();

    }

    @Override
    public List<ReservationInfo> reserveSeats(List<Long> seatIdList, String tokenString) {
        Token token = tokenService.find(tokenString);
        List<Reservation> reservations = concertService.reserveSeats(seatIdList, token.getUserId());

        return reservations.stream()
                .map(reservation -> new ReservationInfo(
                        reservation.getSeatId(),
                        reservation.getReservedBy(),
                        reservation.getReservedAt()))
                .toList();
    }

    @Override
    public void resetReservation() {
        concertService.resetReservation();
    }
}
