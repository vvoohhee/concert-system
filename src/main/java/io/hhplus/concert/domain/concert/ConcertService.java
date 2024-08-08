package io.hhplus.concert.domain.concert;

import io.hhplus.concert.common.enums.ErrorCode;
import io.hhplus.concert.common.enums.ReservationStatusType;
import io.hhplus.concert.common.exception.CustomException;
import io.hhplus.concert.common.util.RestPage;
import io.hhplus.concert.domain.concert.dto.ConcertOptionInfo;
import io.hhplus.concert.domain.concert.dto.SeatPriceInfo;
import io.hhplus.concert.domain.concert.model.Concert;
import io.hhplus.concert.domain.concert.model.ConcertOption;
import io.hhplus.concert.domain.concert.model.Reservation;
import io.hhplus.concert.domain.concert.model.Seat;
import io.hhplus.concert.infrastructure.config.CacheNameValue;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    /**
     * 콘서트 리스트 조회
     *
     * @param reserveAt 예약할 날짜 일시
     * @return List<ConcertOption> 콘서트 공연(회차) 리스트
     */
    @Transactional
    public Page<Concert> findConcerts(LocalDateTime reserveAt) {
        Pageable pageable = PageRequest.of(0, 30, Sort.by("startAt"));
        Page<Concert> concerts = concertRepository.findAvailableConcerts(reserveAt, pageable);

        return new RestPage<>(concerts);
    }

    /**
     * 캐시를 이용한 콘서트 리스트 조회
     *
     * @param reserveAt 예약할 날짜 일시
     * @return List<ConcertOption> 콘서트 공연(회차) 리스트
     */
    @Cacheable(cacheNames = CacheNameValue.CONCERTS, key = "#reserveAt", cacheManager = "cacheManager")
    public Page<Concert> findConcertsWithCache(LocalDateTime reserveAt) {
        Pageable pageable = PageRequest.of(0, 30, Sort.by("startAt"));
        Page<Concert> concerts = concertRepository.findAvailableConcerts(reserveAt, pageable);

        return new RestPage<>(concerts);
    }

    /**
     * 특정 콘서트의 옵션(공연) 리스트 조회
     * @param concertId
     * @return ConcertOptions 리스트
     */
    @Transactional
    public List<ConcertOption> findConcertOptions(Long concertId) {
        return concertRepository.findConcertOptionsByConcertId(concertId);
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
     * @return LocalDateTime 예약 성공한 시각
     */
    @Transactional
    public boolean reserveSeats(List<Long> seatIdList) {
        // 데드락 방지로 좌석 ID를 오름차순 정렬
        Collections.sort(seatIdList);

        // 여러 좌석에 동시에 락을 걸지 않고, 하나씩 락을 걸어 상태 확인하고 예외처리 하기 위해 SELECT WHERE IN() 에서 for문으로 변경
        for (Long seatId : seatIdList) {
            Seat seat = concertRepository.findSeatByIdWithPessimisticLock(seatId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NO_DATA));

            if (!seat.getStatus().equals(ReservationStatusType.AVAILABLE)) {
                throw new CustomException(ErrorCode.RESERVATION_CONFLICT);
            }

            seat.reserve();
        }

        return true;
    }

    /**
     * 예약 데이터 생성 메서드
     * @param seatIdList 좌석 상태 변경에 성공한 좌석 ID 리스트
     * @param userId 예약을 요청한 유저 ID
     * @return List<Reservation> 예약 내용 리스트
     */
    @Transactional
    public List<Reservation> makeReservation(List<Long> seatIdList, Long userId) {
        List<Reservation> reservations = new ArrayList<>();
        LocalDateTime reservedAt = LocalDateTime.now();
        for (Long seatId : seatIdList) {
            reservations.add(new Reservation(seatId, userId, reservedAt));
        }

        List<Reservation> result = concertRepository.saveReservations(reservations);

        if (reservations.size() != result.size()) throw new CustomException(ErrorCode.RESERVATION_PARTIALLY_FAIL);

        return result;
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

            if (reservations.isEmpty()) seat.setStatus(ReservationStatusType.AVAILABLE);

            for (Reservation reservation : reservations) {
                if (reservation.getReservedAt().isBefore(reservationLifetime)) {
                    seat.setStatus(ReservationStatusType.AVAILABLE);
                    concertRepository.deleteReservationById(reservation.getId());
                }
            }
        }
    }

    public void undoReserveSeat(List<Long> seatIdList) {
        concertRepository.updateSeatStatusByIdIn(seatIdList, ReservationStatusType.AVAILABLE);
    }
}
