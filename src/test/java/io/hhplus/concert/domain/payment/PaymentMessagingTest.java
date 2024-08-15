package io.hhplus.concert.domain.payment;

import io.hhplus.concert.common.enums.OutboxStatus;
import io.hhplus.concert.domain.payment.dto.PaymentHistoryCommand;
import io.hhplus.concert.domain.payment.event.PaymentSuccessEvent;
import io.hhplus.concert.domain.payment.event.kafka.PaymentKafkaMessageProducer;
import io.hhplus.concert.infrastructure.db.payment.PaymentJpaRepository;
import io.hhplus.concert.infrastructure.db.payment.PaymentOutboxJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"}
)
public class PaymentMessagingTest {
    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

    @Autowired
    private PaymentOutboxJpaRepository paymentOutboxJpaRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentHistoryService paymentHistoryService;

    @Autowired
    private PaymentKafkaMessageProducer paymentKafkaMessageProducer;

    @BeforeEach
    void setUp() {
        paymentHistoryService.deleteAllPaymentHistories();
    }

    @Test
    @DisplayName("메시지가 제대로 컨슘되어 결제 이력이 생성되었는지 확인")
    void consumePaymentSuccessEvent() throws InterruptedException {
        // given
        List<PaymentHistoryCommand> paymentHistoryCommands = List.of(
                new PaymentHistoryCommand(1L, 10000),
                new PaymentHistoryCommand(2L, 20000),
                new PaymentHistoryCommand(3L, 30000)
        );

        PaymentSuccessEvent event = new PaymentSuccessEvent(paymentHistoryCommands);

        // when
        paymentService.initOutboxMessage(PaymentSuccessEvent.topic, event, event.getIdentifier());
        paymentKafkaMessageProducer.success(event); // 카프카 결제성공 메시지 발행

        // wait
        Thread.sleep(3000); // listener가 메시지를 컨슘하고 로직 수행을 완료하기까지 기다리기 위해 3초 sleep

        // then
        List<Payment> payments = paymentJpaRepository.findAll();
        PaymentOutbox outbox = paymentOutboxJpaRepository.findPaymentOutboxByIdentifier(event.getIdentifier());
        assertEquals(event.getIdentifier(), outbox.getIdentifier());
        assertEquals(OutboxStatus.PUBLISHED, outbox.getStatus());
        assertEquals(paymentHistoryCommands.size(), payments.size());
    }
}
