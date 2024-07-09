package io.hhplus.concert.presentation.concert.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ConcertOptionListDto {
    public record Response(
            List<ConcertOptionDto> concertOptionDtoList
    ) {}

    public record ConcertOptionDto(
            Long concertOptionId,
            String name,
            int seatQuantity,
            Long price,
            int purchaseLimit,
            LocalDateTime reserveFrom,
            LocalDateTime startAt,
            LocalDateTime endAt
    ) {}
}
