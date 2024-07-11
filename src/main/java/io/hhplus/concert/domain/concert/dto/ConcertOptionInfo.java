package io.hhplus.concert.domain.concert.dto;

import java.time.LocalDateTime;

public record ConcertOptionInfo(
        Long id,
        String concertTitle,
        Integer price,
        int seatQuantity,
        int purchaseLimit,
        LocalDateTime reserveFrom,
        LocalDateTime reserveUntil,
        LocalDateTime startAt,
        LocalDateTime endAt
) {
}
