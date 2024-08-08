package io.hhplus.concert.interfaces.presentation.concert.dto;

import io.hhplus.concert.domain.concert.model.ConcertOption;

public record ConcertOptionDto(
        Long id,
        Integer price,
        Integer seatQuantity,
        Integer purchaseLimit
) {
    public static ConcertOptionDto of(ConcertOption option) {
        return new ConcertOptionDto(
                option.getId(),
                option.getPrice(),
                option.getSeatQuantity(),
                option.getPurchaseLimit());
    }
}
