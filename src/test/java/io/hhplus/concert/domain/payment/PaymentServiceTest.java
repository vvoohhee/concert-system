package io.hhplus.concert.domain.payment;

import io.hhplus.concert.common.enums.PaymentStatus;
import io.hhplus.concert.domain.concert.dto.SeatPriceInfo;
import io.hhplus.concert.domain.payment.dto.TicketInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void billingTest() {
        Long userId = 1L;
        List<SeatPriceInfo> seatPriceInfos = List.of(
                new SeatPriceInfo(1L, 50000)
        );

        Ticket ticket1 = new Ticket(1L, 1L, userId, 50000, LocalDateTime.now(), LocalDateTime.now());
        Payment payment1 = new Payment(ticket1.getId(), ticket1.getPrice(), PaymentStatus.PAID);

        when(paymentRepository.saveTicket(any(Ticket.class))).thenReturn(ticket1);
        when(paymentRepository.savePayment(any(Payment.class))).thenReturn(payment1);

        List<TicketInfo> result = paymentService.billing(userId, seatPriceInfos);

        assertEquals(1, result.size());
    }
}
