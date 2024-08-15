package io.hhplus.concert.domain.payment;

import io.hhplus.concert.common.enums.OutboxStatus;
import io.hhplus.concert.domain.payment.dto.PaymentHistoryCommand;
import io.hhplus.concert.domain.payment.event.PaymentSuccessEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentUnitTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void TicketCreationTest_성공() {
        Long validSeatId = 1L;
        Long validUserId = 1L;
        Integer validPrice = 1000;

        Ticket ticket = new Ticket(validSeatId, validUserId, validPrice);

        assertNotNull(ticket);
        assertEquals(validSeatId, ticket.getSeatId());
        assertEquals(validUserId, ticket.getUserId());
        assertEquals(validPrice, ticket.getPrice());
        assertNotNull(ticket.getCreatedAt());
        assertNotNull(ticket.getUpdatedAt());
    }

    @Test
    void TicketCreationTest_실패_좌석ID가_널() {
        Long invalidSeatId = null;
        Long validUserId = 1L;
        Integer validPrice = 1000;

        assertThrows(IllegalArgumentException.class, () -> new Ticket(invalidSeatId, validUserId, validPrice));
    }

    @Test
    void TicketCreationTest_실패_유저ID가_널() {
        Long validSeatId = 1L;
        Long invalidUserId = null;
        Integer validPrice = 1000;

        assertThrows(IllegalArgumentException.class, () -> new Ticket(validSeatId, invalidUserId, validPrice));
    }

    @Test
    void PaymentOutbox_INIT_데이터생성_성공() {
        // given
        List<PaymentHistoryCommand> commands = List.of(
                new PaymentHistoryCommand(1L, 1000),
                new PaymentHistoryCommand(2L, 2000)
        );
        PaymentSuccessEvent event = new PaymentSuccessEvent(commands);

        // when
        when(paymentRepository.initOutbox(any(PaymentOutbox.class)))
                .thenAnswer(invocation -> {
                    PaymentOutbox argument = invocation.getArgument(0);
                    return new PaymentOutbox(
                            1L,
                            argument.getTopic(),
                            argument.getMessage(),
                            OutboxStatus.INIT,
                            argument.getIdentifier(),
                            LocalDateTime.now());
                });

        PaymentOutbox result = paymentService.initOutboxMessage(PaymentSuccessEvent.topic, event, event.getIdentifier());

        // then
        assertEquals(event.getIdentifier(), result.getIdentifier());
        assertEquals(PaymentSuccessEvent.topic, result.getTopic());
        assertEquals(OutboxStatus.INIT, result.getStatus());
    }

    @Test
    void PaymentOutbox_Published_상태로_데아터_변경_성공() {
        // given
        List<PaymentHistoryCommand> commands = List.of(
                new PaymentHistoryCommand(5L, 10000)
        );
        PaymentSuccessEvent event = new PaymentSuccessEvent(commands);
        PaymentOutbox outbox = new PaymentOutbox(PaymentSuccessEvent.topic, event, event.getIdentifier());

        // when
        when(paymentRepository.findOutboxByIdentifier(event.getIdentifier())).thenReturn(outbox);
        boolean result = paymentService.markOutboxAsPublished(event.getIdentifier());

        // then
        assertTrue(result);
    }
}
