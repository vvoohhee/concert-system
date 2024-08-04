package io.hhplus.concert.application.concert;

import io.hhplus.concert.domain.concert.dto.ConcertOptionInfo;
import io.hhplus.concert.domain.concert.dto.ReservationInfo;
import io.hhplus.concert.domain.concert.dto.SeatInfo;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface UserConcertService {
    List<ConcertOptionInfo> findConcerts(LocalDateTime startAt);

    List<SeatInfo> findSeats(Long concertOptionId);

    List<ReservationInfo> reserveSeats(List<Long> seatIdList, String token);

    void resetReservation();

}
