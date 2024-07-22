package io.hhplus.concert.domain.concert;

import io.hhplus.concert.common.enums.ErrorCode;
import io.hhplus.concert.common.enums.ReservationStatusType;
import io.hhplus.concert.common.exception.CustomException;
import io.hhplus.concert.domain.concert.dto.ConcertOptionInfo;
import io.hhplus.concert.domain.concert.dto.SeatPriceInfo;
import io.hhplus.concert.domain.concert.model.ConcertOption;
import io.hhplus.concert.domain.concert.model.Reservation;
import io.hhplus.concert.domain.concert.model.Seat;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    /**
     * 콘서트 공연(회차) 리스트 조회
     *
     * @param reserveAt 예약할 날짜 일시
     * @return List<ConcertOption> 콘서트 공연(회차) 리스트
     */
    @Transactional
    public List<ConcertOptionInfo> findConcerts(LocalDateTime reserveAt) {
        List<ConcertOption> concertOptions = concertRepository.findAvailableConcertOptions(reserveAt);

        return concertOptions.stream()
                .map(option -> new ConcertOptionInfo(
                        option.getId(),
                        option.getConcert().getTitle(),
                        option.getPrice(),
                        option.getSeatQuantity(),
                        option.getPurchaseLimit(),
                        option.getReserveFrom(),
                        option.getReserveUntil(),
                        option.getStartAt(),
                        option.getEndAt()))
                .toList();
    }

    /**
     * 특정 콘서트 공연(회차)의 좌석 리스트 조회
     *
     * @param concertOptionId
     * @return
     */
    public List<Seat> findSeats(Long concertOptionId) {
        return concertRepository.findSeatByConcertOptionId(concertOptionId);
    }

    /**
     * 좌석 예약 메서드
     *
     * @param seatIdList 좌석테이블 ID 리스트
     * @return List<Reservation> 예약 성공한 좌석에 대한 예약 리스트
     */
    @Transactional
    public List<Reservation> reserveSeats(List<Long> seatIdList, Long userId) {
        List<Seat> seats = concertRepository.findSeatByIdIn(seatIdList);

        seats = seats.stream()
                .filter(seat -> seat.getStatus().equals(ReservationStatusType.AVAILABLE))
                .toList();

        if (seats.size() != seatIdList.size()) {
            throw new CustomException(ErrorCode.RESERVATION_CONFLICT);
        }

        List<Reservation> reservationList = new ArrayList<>();
        LocalDateTime reservedAt = LocalDateTime.now();

        for (Seat seat : seats) {
            seat.setStatus(ReservationStatusType.TEMPORARY);

            Reservation reservation = new Reservation(seat.getId(), userId, reservedAt);
            reservation = concertRepository.saveReservation(reservation);
            reservationList.add(reservation);
        }

        return reservationList;
    }

    /**
     * 사용자가 예약한 하나 이상의 좌석 정보를 조회
     *
     * @param userId 사용자 PK
     * @return List<SeatPriceInfo> 사용자의 예약 리스트 조회
     */
    public List<Reservation> findUserReservations(Long userId) {
        return concertRepository.findReservationByReservedBy(userId);
    }

    /**
     * 하나 이상의 좌석에 대해 가격 정보를 조회
     *
     * @return List<SeatPriceInfo> 좌석별 가격 정보 리스트
     */
    public List<SeatPriceInfo> findReservationPrice(List<Long> seatIds) {
        return concertRepository.findSeatPriceInfoBySeatIdIn(seatIds);
    }

    @Transactional
    public void resetReservation() {
        List<Seat> temporarilyReservedSeats = concertRepository.findSeatByStatus(ReservationStatusType.TEMPORARY);

        if (temporarilyReservedSeats.isEmpty()) return;

        LocalDateTime reservationLifetime = LocalDateTime.now().minusMinutes(Reservation.LIFETIME);

        for (Seat seat : temporarilyReservedSeats) {
            List<Reservation> reservations = concertRepository.findReservationBySeatId(seat.getId());

            if (reservations.isEmpty()) continue;

            for (Reservation reservation : reservations) {
                if (reservation.getReservedAt().isBefore(reservationLifetime)) {
                    seat.setStatus(ReservationStatusType.AVAILABLE);
                    concertRepository.deleteReservationById(reservation.getId());
                }
            }
        }
    }
}
