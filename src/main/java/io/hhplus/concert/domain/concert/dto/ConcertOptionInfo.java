package io.hhplus.concert.domain.concert.dto;

public record ConcertOptionInfo(
        Long id,
        Integer price,
        Integer seatQuantity,
        Integer purchaseLimit
) {
}
