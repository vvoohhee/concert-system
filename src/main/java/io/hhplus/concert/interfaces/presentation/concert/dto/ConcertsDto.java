package io.hhplus.concert.interfaces.presentation.concert.dto;

import io.hhplus.concert.domain.concert.dto.ConcertOptionInfo;

import java.time.LocalDateTime;
import java.util.List;

public class ConcertsDto {
    public record Response(
            List<ConcertOptionDto> concertOptionDtoList
    ) {
        public static Response of(List<ConcertOptionInfo> options) {
            List<ConcertOptionDto> concertOptionDtoList = options.stream()
                    .map(option -> new ConcertOptionDto(
                            option.id(),
                            option.concertTitle(),
                            option.price(),
                            option.seatQuantity(),
                            option.purchaseLimit(),
                            option.reserveFrom(),
                            option.reserveUntil(),
                            option.startAt(),
                            option.endAt()))
                    .toList();

            return new Response(concertOptionDtoList);
        }
    }

    public record ConcertOptionDto(
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
}
