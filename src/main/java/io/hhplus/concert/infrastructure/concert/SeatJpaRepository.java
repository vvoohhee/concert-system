package io.hhplus.concert.infrastructure.concert;

import io.hhplus.concert.common.enums.ReservationStatusType;
import io.hhplus.concert.domain.concert.model.Seat;
import io.hhplus.concert.infrastructure.concert.dto.SeatPriceProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatJpaRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByConcertOptionId(Long concertOptionId);

    List<Seat> findByIdIn(List<Long> ids);

    List<Seat> findByStatus(ReservationStatusType status);

    @Query("SELECT s.id AS seatId, co.price as price " +
            "FROM Seat s " +
            "JOIN ConcertOption co ON s.concertOptionId = co.id " +
            "WHERE s.id IN :ids")
    List<SeatPriceProjection> findSeatsProjectionByIdIn(@Param("ids") List<Long> ids);
}
