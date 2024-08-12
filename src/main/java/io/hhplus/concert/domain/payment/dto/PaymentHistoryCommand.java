package io.hhplus.concert.domain.payment.dto;

import io.hhplus.concert.common.enums.PaymentStatus;
import lombok.Getter;

@Getter
public class PaymentHistoryCommand {
    private Long ticketId;
    private Integer price;
    private PaymentStatus status;

    public PaymentHistoryCommand(Long ticketId, Integer price) {
        this.ticketId = ticketId;
        this.price = price;
        this.status = PaymentStatus.PAID;
    }
}
