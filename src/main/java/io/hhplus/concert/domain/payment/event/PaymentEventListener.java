package io.hhplus.concert.domain.payment.event;

import io.hhplus.concert.domain.payment.PaymentService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PaymentEventListener {
    private final PaymentService paymentService;

    public PaymentEventListener(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void paymentSuccessHandler(PaymentSuccessEvent event) {
        paymentService.savePaymentHistories(event.getPaymentHistoryCommands());
    }
}
