package io.hhplus.concert.infrastructure.payment;

import io.hhplus.concert.domain.payment.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketJpaRepository extends JpaRepository<Ticket, Long> {
}
