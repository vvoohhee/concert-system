package io.hhplus.concert.domain.payment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PaymentUnitTest {

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

}
