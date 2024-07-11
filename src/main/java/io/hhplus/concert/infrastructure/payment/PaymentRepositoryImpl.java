package io.hhplus.concert.infrastructure.payment;

import io.hhplus.concert.domain.payment.Payment;
import io.hhplus.concert.domain.payment.PaymentRepository;
import io.hhplus.concert.domain.payment.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;
    private final TicketJpaRepository ticketJpaRepository;


    @Override
    public Ticket saveTicket(Ticket ticket) {
        return ticketJpaRepository.save(ticket);
    }

    @Override
    public Payment savePayment(Payment payment) {
        return paymentJpaRepository.save(payment);
    }
}
