package io.hhplus.concert.infrastructure.concert;

import io.hhplus.concert.domain.concert.model.Concert;
import io.hhplus.concert.domain.concert.model.ConcertOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertOptionJpaRepository extends JpaRepository<ConcertOption, Long> {
    @Query("SELECT co " +
            "FROM ConcertOption co " +
            "WHERE co.reserveFrom < :reserveAt " +
            "AND co.reserveUntil > :reserveAt")
    List<ConcertOption> findAvailableConcertOptions(LocalDateTime reserveAt);
}
