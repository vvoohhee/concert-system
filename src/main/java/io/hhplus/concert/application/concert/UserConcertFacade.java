package io.hhplus.concert.application.concert;

import io.hhplus.concert.common.enums.ErrorCode;
import io.hhplus.concert.common.exception.CustomException;
import io.hhplus.concert.domain.concert.ConcertService;
import io.hhplus.concert.domain.concert.dto.ConcertOptionInfo;
import io.hhplus.concert.domain.concert.dto.ReservationInfo;
import io.hhplus.concert.domain.concert.dto.SeatInfo;
import io.hhplus.concert.domain.concert.model.Reservation;
import io.hhplus.concert.domain.concert.model.Seat;
import io.hhplus.concert.domain.token.Token;
import io.hhplus.concert.domain.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
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
        concertService.reserveSeats(seatIdList);

        // 좌석 테이블에 비관락을 걸어 예약 상태를 확인 후 좌석을 예약중으로 변경
        // 이후 Reservation 테이블에 사용자의 예약 내역 생성
        List<Reservation> reservations;
        try {
            reservations = concertService.makeReservation(seatIdList, token.getUserId());
        } catch (CustomException e) {
            // 보상 트랜잭션 구현
            concertService.undoReserveSeat(seatIdList);
            throw e;
        } catch (Exception e) {
            // 보상 트랜잭션 구현
            log.error("[콘서트 예약 API][유저ID : {}] 알 수 없는 에러로 콘서트 예약 실패", token.getUserId());
            concertService.undoReserveSeat(seatIdList);
            throw e;
        }

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
