package io.hhplus.concert.presentation.concert.dto;

import java.util.List;

public class GetConcertSeatListDto {
    public record SeatDto(
            Long id,
            int number,
            int status
    ) {}

    public record Response(
            Long concertOptionId,
            List<SeatDto> seatDtoList
    ) {}
}
