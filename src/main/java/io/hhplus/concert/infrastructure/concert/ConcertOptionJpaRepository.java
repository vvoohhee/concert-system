package io.hhplus.concert.infrastructure.concert;

import io.hhplus.concert.domain.concert.model.ConcertOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ConcertOptionJpaRepository extends JpaRepository<ConcertOption, Long> {

    @Query("SELECT co FROM ConcertOption co WHERE co.reserveFrom < :reserveAt AND co.reserveUntil > :reserveAt")
    Page<ConcertOption> findAvailableConcertOptions(@Param("reserveAt") LocalDateTime reserveAt, Pageable pageable);

    @Query("SELECT co " +
            "FROM ConcertOption co " +
            "JOIN Seat s ON s.concertOptionId = co.id " +
            "WHERE s.id = :seatId")
    ConcertOption findTopBySeatId(Long seatId);
}
