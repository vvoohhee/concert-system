package io.hhplus.concert.domain.payment;

public interface PaymentRepository {
    Ticket saveTicket(Ticket ticket);

    Payment savePayment(Payment payment);
}
