package io.hhplus.concert.domain.payment.event.spring;

import io.hhplus.concert.domain.payment.PaymentHistoryService;
import io.hhplus.concert.domain.payment.event.PaymentEventListener;
import io.hhplus.concert.domain.payment.event.PaymentSuccessEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PaymentSpringEventListener implements PaymentEventListener {
    private final PaymentHistoryService paymentHistoryService;

    public PaymentSpringEventListener(PaymentHistoryService paymentHistoryService) {
        this.paymentHistoryService = paymentHistoryService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Override
    public void paymentSuccessHandler(PaymentSuccessEvent event) {
        paymentHistoryService.savePaymentHistories(event.getPaymentHistoryCommands());
    }
}
