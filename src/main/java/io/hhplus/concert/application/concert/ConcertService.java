package io.hhplus.concert.application.concert;

import io.hhplus.concert.presentation.concert.dto.ConcertOptionListDto;
import io.hhplus.concert.presentation.concert.dto.ConcertReserveDto;
import io.hhplus.concert.presentation.concert.dto.GetConcertSeatListDto;

import java.time.LocalDateTime;

public interface ConcertService {
    ConcertOptionListDto.Response findConcertOptions(Long concertId, String authorization);

    GetConcertSeatListDto.Response findSeats(Long concertId, LocalDateTime startAt, String authorization);

    ConcertReserveDto.Response reserveSeats(ConcertReserveDto.Request request, String authorization);
}
