package io.hhplus.concert.domain.payment;

import java.util.List;

public interface PaymentRepository {
    List<Ticket> saveTickets(List<Ticket> tickets);

    List<Payment> savePayments(List<Payment> payments);
}
