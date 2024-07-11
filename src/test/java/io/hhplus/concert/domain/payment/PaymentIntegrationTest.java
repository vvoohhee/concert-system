package io.hhplus.concert.domain.payment;

import io.hhplus.concert.application.payment.UserPaymentFacade;
import io.hhplus.concert.common.enums.TokenStatusType;
import io.hhplus.concert.domain.concert.ConcertService;
import io.hhplus.concert.domain.concert.dto.SeatPriceInfo;
import io.hhplus.concert.domain.payment.dto.TicketInfo;
import io.hhplus.concert.domain.token.Token;
import io.hhplus.concert.domain.token.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PaymentIntegrationTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private ConcertService concertService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private UserPaymentFacade userPaymentFacade;


    @Test
    void billingTest() {
        Long userId = 1L;
        List<SeatPriceInfo> seatPriceInfos = List.of(
                new SeatPriceInfo(1L, 50000),
                new SeatPriceInfo(2L, 60000)
        );

        Token token = new Token(userId);
        token.setStatus(TokenStatusType.AVAILABLE);

        when(tokenService.find(token.getToken())).thenReturn(token);
        when(concertService.findReservedPrice(userId)).thenReturn(seatPriceInfos);
        when(paymentService.billing(userId, seatPriceInfos)).thenReturn(List.of(
                new TicketInfo(1L, 1L, 50000),
                new TicketInfo(2L, 2L, 60000)
        ));

        // Call the method under test
        List<TicketInfo> result = userPaymentFacade.billing(token.getToken());

        assertEquals(2, result.size());
        assertEquals(new TicketInfo(1L, 1L, 50000), result.get(0));
        assertEquals(new TicketInfo(2L, 2L, 60000), result.get(1));
    }
}
