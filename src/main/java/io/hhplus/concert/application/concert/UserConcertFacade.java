package io.hhplus.concert.application.concert;

import io.hhplus.concert.domain.concert.ConcertService;
import io.hhplus.concert.domain.concert.dto.ConcertOptionInfo;
import io.hhplus.concert.domain.concert.dto.ReservationInfo;
import io.hhplus.concert.domain.concert.dto.SeatInfo;
import io.hhplus.concert.domain.concert.model.Reservation;
import io.hhplus.concert.domain.token.Token;
import io.hhplus.concert.domain.token.TokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserConcertFacade implements UserConcertService {
    private TokenService tokenService;
    private ConcertService concertService;

    @Override
    public List<ConcertOptionInfo> findConcerts(LocalDateTime reserveAt) {
        return concertService.findConcerts(reserveAt);
    }

    @Override
    public List<SeatInfo> findSeats(Long concertOptionId) {
        return concertService.findSeats(concertOptionId);
    }

    @Override
    @Transactional
    public List<ReservationInfo> reserveSeats(List<Long> seatIdList, String tokenString) {
        Token token = tokenService.find(tokenString);
        List<Reservation> reservations = concertService.reserveSeats(seatIdList, token.getUserId());

        List<ReservationInfo> reservationInfoList = new ArrayList<>();
        for (Reservation reservation : reservations) {
            reservationInfoList.add(
                    new ReservationInfo(reservation.getSeatId(), reservation.getReservedBy(), reservation.getReservedAt())
            );
        }

        return reservationInfoList;
    }
}
