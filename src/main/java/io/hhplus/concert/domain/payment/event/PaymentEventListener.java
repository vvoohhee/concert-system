package io.hhplus.concert.domain.payment.event;

public interface PaymentEventListener {
    void paymentSuccessHandler(PaymentSuccessEvent event);
}
