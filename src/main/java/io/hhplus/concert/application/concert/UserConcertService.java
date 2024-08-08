package io.hhplus.concert.application.concert;

import io.hhplus.concert.domain.concert.dto.ReservationInfo;
import io.hhplus.concert.domain.concert.dto.SeatInfo;
import io.hhplus.concert.domain.concert.model.Concert;
import io.hhplus.concert.domain.concert.model.ConcertOption;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface UserConcertService {
    Page<Concert> findConcerts(LocalDateTime startAt);

    List<SeatInfo> findSeats(Long concertOptionId);

    List<ReservationInfo> reserveSeats(List<Long> seatIdList, String token);

    void resetReservation();

    List<ConcertOption> findConcertOptions(Long concertId);
}
