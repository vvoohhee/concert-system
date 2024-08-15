package io.hhplus.concert.infrastructure.db.concert;

import io.hhplus.concert.common.enums.ReservationStatusType;
import io.hhplus.concert.domain.concert.model.Seat;
import io.hhplus.concert.infrastructure.db.concert.dto.SeatPriceProjection;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatJpaRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByConcertOptionIdOrderByNumberAsc(Long concertOptionId);

    List<Seat> findByIdIn(List<Long> ids);

    List<Seat> findByStatus(ReservationStatusType status);

    @Query("SELECT s.id AS seatId, co.price as price " +
            "FROM Seat s " +
            "JOIN ConcertOption co ON s.concertOptionId = co.id " +
            "WHERE s.id IN :ids")
    List<SeatPriceProjection> findSeatsProjectionByIdIn(@Param("ids") List<Long> ids);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id = :id")
    Optional<Seat> findSeatByIdWithPessimisticLock(Long id);

    @Modifying
    @Query("UPDATE Seat s SET s.status = :status WHERE s.id IN :ids")
    List<Seat> updateStatusByIdIn(List<Long> ids, ReservationStatusType status);
}
