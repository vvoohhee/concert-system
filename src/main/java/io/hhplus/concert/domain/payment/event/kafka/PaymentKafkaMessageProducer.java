package io.hhplus.concert.domain.payment.event.kafka;

import io.hhplus.concert.domain.payment.event.PaymentEventPublisher;
import io.hhplus.concert.domain.payment.event.PaymentSuccessEvent;
import io.hhplus.concert.infrastructure.kafka.KafkaMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
public class PaymentKafkaMessageProducer implements PaymentEventPublisher {
    private final KafkaMessageProducer kafkaMessageProducer;

    @Override
    public void success(PaymentSuccessEvent event) {
        // 카프카에 메시지 발행
        kafkaMessageProducer.send(PaymentSuccessEvent.topic, event);
    }
}
