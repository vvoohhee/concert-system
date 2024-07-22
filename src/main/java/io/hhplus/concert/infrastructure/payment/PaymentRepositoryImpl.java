package io.hhplus.concert.infrastructure.payment;

import io.hhplus.concert.domain.payment.Payment;
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


    @Override
    public List<Ticket> saveTickets(List<Ticket> tickets) {
        return ticketJpaRepository.saveAll(tickets);
    }

    @Override
    public List<Payment> savePayments(List<Payment> payments) {
        return paymentJpaRepository.saveAll(payments);
    }
}
