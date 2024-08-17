package io.hhplus.concert.infrastructure.db.concert;

import io.hhplus.concert.domain.concert.model.ConcertOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConcertOptionJpaRepository extends JpaRepository<ConcertOption, Long> {

    List<ConcertOption> findByConcertId(Long concertId);

    @Query("SELECT co " +
            "FROM ConcertOption co " +
            "JOIN Seat s ON s.concertOptionId = co.id " +
            "WHERE s.id = :seatId")
    ConcertOption findTopBySeatId(Long seatId);
}
