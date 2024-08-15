package io.hhplus.concert.domain.payment;

import io.hhplus.concert.common.enums.OutboxStatus;
import io.hhplus.concert.domain.payment.dto.PaymentHistoryCommand;
import io.hhplus.concert.domain.payment.event.PaymentSuccessEvent;
import io.hhplus.concert.domain.payment.event.kafka.PaymentKafkaMessageProducer;
import io.hhplus.concert.infrastructure.db.payment.PaymentJpaRepository;
import io.hhplus.concert.infrastructure.db.payment.PaymentOutboxJpaRepository;
import io.hhplus.concert.interfaces.scheduler.PaymentOutboxScheduler;
import org.apache.kafka.common.Uuid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

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

    @Autowired
    private PaymentOutboxScheduler paymentOutboxScheduler;

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
        assertEquals(paymentHistoryCommands.size(), payments.size());
    }

    @Test
    @DisplayName("Outbox에서 5분 이상 init 상태로 머물러있는 메시지들에 대해 재시도하는 스케줄러가 정말 동작하는지")
    void testRetryInitMessagesIsCalled() {
        await().atMost(6, TimeUnit.MINUTES).untilAsserted(() -> verify(paymentOutboxScheduler).retryInitMessages());
    }

    @Test
    @DisplayName("Outbox에서 5분 이상 init 상태로 머물러있는 메시지들에 대해 재시도")
    void testRetryInitMessagesResult() throws InterruptedException {
        //given
        PaymentHistoryCommand command = new PaymentHistoryCommand(1L, 23000);
        PaymentSuccessEvent event = new PaymentSuccessEvent(List.of(command));

        PaymentOutbox outbox = new PaymentOutbox(
                PaymentSuccessEvent.topic,
                event,
                event.getIdentifier());
        outbox = paymentOutboxJpaRepository.save(outbox);

        outbox.setCreatedAt(LocalDateTime.now().minusMinutes(100));
        paymentOutboxJpaRepository.save(outbox);

        // when
        paymentOutboxScheduler.retryInitMessages();
        Thread.sleep(3000);

        // then
        PaymentOutbox updated = paymentOutboxJpaRepository.findPaymentOutboxByIdentifier(event.getIdentifier());
        assertEquals(OutboxStatus.PUBLISHED, updated.getStatus());

    }
}
