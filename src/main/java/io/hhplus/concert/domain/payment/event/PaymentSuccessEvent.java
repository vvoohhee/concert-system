package io.hhplus.concert.domain.payment.event;

import io.hhplus.concert.domain.payment.dto.PaymentHistoryCommand;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PaymentSuccessEvent {
    public static final String topic = "payment-success";
    private List<PaymentHistoryCommand> paymentHistoryCommands;
    private String identifier; // 각각의 메시지를 구분하기 위한 uuid를 추가

    public PaymentSuccessEvent(List<PaymentHistoryCommand> paymentHistoryCommands) {
        this.paymentHistoryCommands = paymentHistoryCommands;
        this.identifier = UUID.randomUUID().toString();
    }
}