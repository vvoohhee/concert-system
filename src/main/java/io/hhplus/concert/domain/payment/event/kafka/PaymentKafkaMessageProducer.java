package io.hhplus.concert.domain.payment.event.kafka;

import io.hhplus.concert.domain.payment.event.PaymentEventListener;
import io.hhplus.concert.domain.payment.event.PaymentSuccessEvent;
import io.hhplus.concert.infrastructure.kafka.KafkaMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Primary
@Component
@RequiredArgsConstructor
public class PaymentKafkaMessageProducer implements PaymentEventListener {
    private final KafkaMessageProducer kafkaMessageProducer;

    @Override
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void paymentSuccessHandler(PaymentSuccessEvent event) {
        // 카프카에 메시지 발행
        kafkaMessageProducer.send(PaymentSuccessEvent.topic, event);
    }
}
