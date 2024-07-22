package io.hhplus.concert.interfaces.presentation.concert.dto;

import io.hhplus.concert.domain.concert.dto.ReservationInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConcertReserveDto {
    public record Request(
            List<Long> seatIdList
    ) {
    }

    public record Response(
            List<ReservationDto> reservedList
    ) {
        public static Response of(List<ReservationInfo> reservedList) {
            List<ReservationDto> reservationDtoList = new ArrayList<>();
            for (ReservationInfo reservation : reservedList) {
                reservationDtoList.add(new ReservationDto(reservation.seatId(), reservation.reservedBy(), reservation.reservedAt()));
            }
            return new Response(reservationDtoList);
        }
    }

    public record ReservationDto(
            Long seatId,
            Long reservedBy,
            LocalDateTime reservedAt
    ) {
    }
}
