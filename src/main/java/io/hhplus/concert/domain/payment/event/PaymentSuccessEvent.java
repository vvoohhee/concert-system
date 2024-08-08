package io.hhplus.concert.domain.payment.event;

import io.hhplus.concert.domain.payment.dto.PaymentHistoryCommand;
import lombok.Getter;

import java.util.List;

@Getter
public class PaymentSuccessEvent {
    private List<PaymentHistoryCommand> paymentHistoryCommands;

    public PaymentSuccessEvent(List<PaymentHistoryCommand> paymentHistoryCommands) {
        this.paymentHistoryCommands = paymentHistoryCommands;
    }
}