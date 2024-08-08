package io.hhplus.concert.infrastructure.concert;

import io.hhplus.concert.domain.concert.model.Concert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ConcertJpaRepository extends JpaRepository<Concert, Long> {

    @Query("SELECT c " +
            "FROM Concert c " +
            "WHERE c.reserveFrom < :reserveAt " +
            "AND c.reserveUntil > :reserveAt")
    Page<Concert> findAvailableConcerts(@Param("reserveAt") LocalDateTime reserveAt, Pageable pageable);
}
