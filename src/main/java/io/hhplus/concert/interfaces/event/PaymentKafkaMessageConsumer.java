package io.hhplus.concert.interfaces.event;

import io.hhplus.concert.application.payment.UserPaymentService;
import io.hhplus.concert.common.util.ObjectStringConverter;
import io.hhplus.concert.domain.payment.PaymentHistoryService;
import io.hhplus.concert.domain.payment.event.PaymentSuccessEvent;
import io.hhplus.concert.infrastructure.config.KafkaConsumerConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentKafkaMessageConsumer {

    private final UserPaymentService userPaymentService;
    private final PaymentHistoryService paymentHistoryService;

    @KafkaListener(topics = PaymentSuccessEvent.topic,
            groupId = KafkaConsumerConfig.paymentSuccessOutbox)
    public void updatePaymentSuccessOutbox(String message) {
        log.info("[Kafka][Consume payment-success] 메시지 컨슘 성공, updatePaymentSuccessOutbox 로직 수행 시작");
        log.info("[Kafka][Consume payment-success] 메시지 : {}", message);
        PaymentSuccessEvent event = ObjectStringConverter.fromJson(message, PaymentSuccessEvent.class);
        userPaymentService.markOutboxAsPublished(event.getIdentifier());
    }

    @KafkaListener(topics = PaymentSuccessEvent.topic,
            groupId = KafkaConsumerConfig.paymentSuccessBusiness)
    public void consumePaymentSuccessMessage(String message) {
        log.info("[Kafka][Consume payment-success] 메시지 컨슘 성공, consumePaymentSuccessMessage 로직 수행 시작");
        log.info("[Kafka][Consume payment-success] 메시지 : {}", message);
        PaymentSuccessEvent event = ObjectStringConverter.fromJson(message, PaymentSuccessEvent.class);
        paymentHistoryService.savePaymentHistories(event.getPaymentHistoryCommands());
    }

}
