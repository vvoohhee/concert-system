package io.hhplus.concert.presentation.concert.dto;

import java.util.List;

public class ConcertReserveDto {
    public record Request(
            List<Long> seatList
    ) {}

    public record Response(
            int status,
            String description
    ) {}
}
