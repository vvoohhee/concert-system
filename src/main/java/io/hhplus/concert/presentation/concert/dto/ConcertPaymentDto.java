package io.hhplus.concert.presentation.concert.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ConcertPaymentDto {
    public record Ticket(
            Long id,
            ConcertInfo concertInfo,
            SeatInfo seatInfo
    ) {}

    public record ConcertInfo(
            Long concertOptionId,
            Integer price,
            LocalDateTime startAt,
            LocalDateTime endAt
    ) {}

    public record SeatInfo(
            Long id,
            Integer number
    ) {}

    public record Response(
            int status,
            String description,
            List<Ticket> ticketList
    ) {}
}
