package io.hhplus.concert.infrastructure.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka // Spring에게 Kafka를 사용하겠다고 알림 => Kafka 관련 Bean들을 활성화
@Configuration
public class KafkaConsumerConfig {
    // consumer group-id 지정
    public static final String paymentSuccessOutbox = "payment-success-outbox";
    public static final String paymentSuccessBusiness = "payment-success-business";

    /**
     * Kafka Consumer 생성을 위한 ConsumerFactory 생성
     * ConsumerFactory : Consumer를 생성, 컨슈머가 사용할 config 설정
     * @return ConsumerFactory
     */
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ConsumerConfig.GROUP_ID_CONFIG, paymentSuccessBusiness);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        JsonDeserializer<Object> jsonDeserializer = new JsonDeserializer<>(Object.class, false);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), jsonDeserializer);
    }

    /**
     * Kafka 메시지를 소비하기 위한 리스너 컨테이너 팩토리를 생성
     * Kafka 메시지를 비동기적으로 처리하기 위해 필요한 스레드 풀, 예외 처리, 재시도 처리 등을 설정할 수 있음
     * Spring 애플리케이션 컨텍스트가 초기화될 때 호출됨 (Bean이자나) -> 리스너 컨테이너 팩토리를 생성
     * @return ConcurrentKafkaListenerContainerFactory
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }
}
