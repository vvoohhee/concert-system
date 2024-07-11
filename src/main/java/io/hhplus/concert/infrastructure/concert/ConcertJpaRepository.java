package io.hhplus.concert.infrastructure.concert;

import io.hhplus.concert.domain.concert.model.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertJpaRepository extends JpaRepository<Concert, Long> {
}
