package io.hhplus.concert.interfaces.presentation.concert.dto;

import io.hhplus.concert.domain.payment.dto.TicketInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConcertPaymentDto {
    public record Response(
            List<TicketDto> ticketList
    ) {
        public static Response of(List<TicketInfo> ticketList) {
            List<TicketDto> response = new ArrayList<>();
            ticketList.forEach(ticket -> response.add(
                    new TicketDto(
                            ticket.id(),
                            ticket.seatId(),
                            ticket.price()
                    ))
            );
            return new Response(response);
        }
    }

    public record TicketDto(
            Long id,
            Long seatId,
            Integer price
    ) {
    }
}
