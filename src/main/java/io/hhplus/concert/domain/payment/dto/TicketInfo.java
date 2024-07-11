package io.hhplus.concert.domain.payment.dto;

public record TicketInfo(
        Long id,
        Long seatId,
        Integer price
) {
}
