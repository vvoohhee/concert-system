package io.hhplus.concert.domain.payment;

import io.hhplus.concert.common.enums.OutboxStatus;

import java.util.List;

public interface PaymentRepository {
    List<Ticket> saveTickets(List<Ticket> tickets);

    List<Payment> savePayments(List<Payment> payments);

    PaymentOutbox initOutbox(PaymentOutbox outbox);

    List<PaymentOutbox> findOutboxesByStatus(OutboxStatus status);

    PaymentOutbox findOutboxByIdentifier(String identifier);

    void deleteAllPaymentHistories();
}
