package io.hhplus.concert.interfaces.presentation.concert.dto;

import io.hhplus.concert.domain.concert.dto.ConcertOptionInfo;

import java.time.LocalDateTime;
import java.util.List;

public class ConcertsDto {
    public record Response(
            Long id,
            String concertTitle,
            Integer price,
            int seatQuantity,
            int purchaseLimit
    ) {
        public static Response of(ConcertOptionInfo option) {
            return new Response(
                    option.id(),
                    option.concertTitle(),
                    option.price(),
                    option.seatQuantity(),
                    option.purchaseLimit());
        }
    }
}
