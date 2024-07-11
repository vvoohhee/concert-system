package io.hhplus.concert.domain.concert;

import io.hhplus.concert.common.enums.ReservationStatusType;
import io.hhplus.concert.domain.concert.model.Reservation;
import io.hhplus.concert.domain.concert.model.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ConcertScheduler {

    private ConcertRepository concertRepository;

    @Scheduled(fixedRate = 3, timeUnit = TimeUnit.MINUTES)
    public void resetReservation() {
        // 이선좌 상태인 좌석의 예약 시간이 5분 이상 경과한 경우,
        // 좌석의 상태 변경, Reservation 물리적 삭제\

        List<Seat> temporarilyReservedSeats = concertRepository.findSeatByStatus(ReservationStatusType.TEMPORARY);

        if (temporarilyReservedSeats.isEmpty()) return;

        for (Seat seat : temporarilyReservedSeats) {
            List<Reservation> reservations = concertRepository.findReservationBySeatId(seat.getId());

            if (reservations.isEmpty()) continue;

            for (Reservation reservation : reservations) {
                if (reservation.getReservedAt().isAfter(LocalDateTime.now().minusMinutes(Reservation.LIFETIME))
                ) {
                    seat.setStatus(ReservationStatusType.AVAILABLE);
                    concertRepository.deleteReservationById(reservation.getId());
                }
            }
        }
    }

}
