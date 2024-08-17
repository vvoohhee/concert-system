package io.hhplus.concert.domain.payment.event;

public interface PaymentEventPublisher {
    void success(PaymentSuccessEvent event);
}
