package io.hhplus.concert.infrastructure.payment;

import io.hhplus.concert.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {
}
