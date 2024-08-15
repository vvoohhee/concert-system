package io.hhplus.concert.infrastructure.db.payment;

import io.hhplus.concert.common.enums.OutboxStatus;
import io.hhplus.concert.domain.payment.PaymentOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentOutboxJpaRepository extends JpaRepository<PaymentOutbox, Long> {
    List<PaymentOutbox> findPaymentOutboxesByStatus(OutboxStatus status);
    PaymentOutbox findPaymentOutboxByIdentifier(String uuid);
}
