package io.hhplus.concert.domain.payment.event.spring;

import io.hhplus.concert.domain.payment.event.PaymentEventPublisher;
import io.hhplus.concert.domain.payment.event.PaymentSuccessEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class PaymentSpringEventPublisher implements PaymentEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public PaymentSpringEventPublisher(ApplicationEventPublisher paymentKafkaMessageProducer) {
        this.applicationEventPublisher = paymentKafkaMessageProducer;
    }

    @Override
    public void success(PaymentSuccessEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
