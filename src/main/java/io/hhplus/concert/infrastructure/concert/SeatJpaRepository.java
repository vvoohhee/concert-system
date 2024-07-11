package io.hhplus.concert.infrastructure.concert;

import io.hhplus.concert.common.enums.ReservationStatusType;
import io.hhplus.concert.domain.concert.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatJpaRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByConcertOptionId(Long concertOptionId);

    List<Seat> findByIdIn(List<Long> ids);

    List<Seat> findByStatus(ReservationStatusType status);
}
