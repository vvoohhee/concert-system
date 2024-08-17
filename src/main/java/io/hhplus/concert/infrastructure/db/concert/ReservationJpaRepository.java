package io.hhplus.concert.infrastructure.db.concert;

import io.hhplus.concert.domain.concert.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findBySeatId(Long seatId);
    void deleteById(Long id);

    List<Reservation> findByReservedBy(Long userId);
}
