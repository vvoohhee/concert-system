package io.hhplus.concert.infrastructure.db.payment;

import io.hhplus.concert.common.enums.OutboxStatus;
import io.hhplus.concert.domain.payment.Payment;
import io.hhplus.concert.domain.payment.PaymentOutbox;
import io.hhplus.concert.domain.payment.PaymentRepository;
import io.hhplus.concert.domain.payment.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;
    private final TicketJpaRepository ticketJpaRepository;
    private final PaymentOutboxJpaRepository outboxJpaRepository;


    @Override
    public List<Ticket> saveTickets(List<Ticket> tickets) {
        return ticketJpaRepository.saveAll(tickets);
    }

    @Override
    public List<Payment> savePayments(List<Payment> payments) {
        return paymentJpaRepository.saveAll(payments);
    }

    @Override
    public PaymentOutbox initOutbox(PaymentOutbox outbox) {
        return outboxJpaRepository.save(outbox);
    }

    @Override
    public List<PaymentOutbox> findOutboxesByStatus(OutboxStatus status) {
        return outboxJpaRepository.findPaymentOutboxesByStatus(status);
    }

    @Override
    public PaymentOutbox findOutboxByIdentifier(String identifier) {
        return outboxJpaRepository.findPaymentOutboxByIdentifier(identifier);
    }

    @Override
    public void deleteAllPaymentHistories() {
        paymentJpaRepository.truncateTable();
    }
}
