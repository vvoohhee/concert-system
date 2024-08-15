package io.hhplus.concert.infrastructure.kafka;

import io.hhplus.concert.common.util.ObjectStringConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, Object message) {
        String messageString = ObjectStringConverter.toJson(message);
        sendToKafka(topic, messageString);
    }

    private void sendToKafka(String topic, String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("[Kafka][메시지 발행 성공] message : {}, offset : {}", message, result.getRecordMetadata().offset());
            } else {
                log.error("[Kafka][메시지 발생 실패] error : {}", ex.getMessage());
            }
        });
    }
}
