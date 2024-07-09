package io.hhplus.concert.domain.concert;

import io.hhplus.concert.common.enums.ReservationStatusType;
import io.hhplus.concert.common.exception.CustomException;

import java.time.LocalDateTime;
import java.util.Objects;

public class Seat {
    private Long id;
    private Long concertOptionId;
    private Integer number;
    private ReservationStatusType status;
    private Reservation reservation;

    public Seat(Long concertOptionId, Integer number) {
        if (Objects.isNull(concertOptionId)) throw new IllegalArgumentException("유효하지 않은 콘서트 옵션 ID");
        if (number > ConcertOption.DEFAULT_SEAT_QUANTITY || number < 0)
            throw new IllegalArgumentException("유효하지 않은 좌석 번호");
        status = ReservationStatusType.AVAILABLE;
    }

    public Reservation reserve(Long userId) {
        if (!status.equals(ReservationStatusType.AVAILABLE)) throw new CustomException("이미 선택된 좌석입니다.");

        status = ReservationStatusType.TEMPORARY;
        reservation = new Reservation(this.id, userId);
        return reservation;
    }

    public void resetReservation(Long userId) {
        // 이선좌 상태인 좌석의 예약 시간이 5분 이상 경과한 경우
        // 좌석의 상태 변경, Reservation 물리적 삭제
        if (status.equals(ReservationStatusType.TEMPORARY)
                && reservation.getReservedAt().isAfter(LocalDateTime.now().plusMinutes(Reservation.LIFETIME))
        ) {
            status = ReservationStatusType.AVAILABLE;
            reservation = null;
        }
    }
}
