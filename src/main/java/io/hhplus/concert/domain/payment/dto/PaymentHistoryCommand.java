package io.hhplus.concert.domain.payment.dto;

import io.hhplus.concert.common.enums.PaymentStatus;
import io.hhplus.concert.domain.payment.Ticket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class PaymentHistoryCommand {
    private Long ticketId;
    private Integer price;
    private PaymentStatus status;

    public PaymentHistoryCommand(Long ticketId, Integer price) {
        this.ticketId = ticketId;
        this.price = price;
        this.status = PaymentStatus.PAID;
    }

    public static PaymentHistoryCommand fromDomain(Ticket ticket) {
        return new PaymentHistoryCommand(ticket.getId(), ticket.getPrice());
    }
}
