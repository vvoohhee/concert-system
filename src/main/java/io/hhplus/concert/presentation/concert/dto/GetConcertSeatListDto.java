package io.hhplus.concert.presentation.concert.dto;

import io.hhplus.concert.common.enums.ReservationStatusType;
import io.hhplus.concert.domain.concert.dto.SeatInfo;

import java.util.List;

public class GetConcertSeatListDto {
    public record Response(
            List<SeatDto> seatDtoList
    ) {
        public static Response of(List<SeatInfo> seats) {
            List<SeatDto> seatDtoList = seats.stream()
                    .map(seat -> new SeatDto(seat.id(), seat.number(), seat.status()))
                    .toList();

            return new Response(seatDtoList);
        }
    }

    public record SeatDto(
            Long id,
            int number,
            ReservationStatusType status
    ) {
    }
}
