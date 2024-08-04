package io.hhplus.concert.domain.concert.dto;

public record ConcertOptionInfo(
        Long id,
        String concertTitle,
        Integer price,
        Integer seatQuantity,
        Integer purchaseLimit
) {
}
