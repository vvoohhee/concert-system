package io.hhplus.concert.domain.payment;

import io.hhplus.concert.common.enums.PaymentStatus;
import io.hhplus.concert.domain.concert.dto.SeatPriceInfo;
import io.hhplus.concert.domain.payment.dto.TicketInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public List<TicketInfo> billing(Long userId, List<SeatPriceInfo> priceInfoList) {

        List<TicketInfo> ticketInfoList = new ArrayList<>();
        for(SeatPriceInfo seatPriceInfo : priceInfoList) {
            Ticket ticket = new Ticket(seatPriceInfo.seatId(), userId, seatPriceInfo.price());
            ticket = paymentRepository.saveTicket(ticket);
            ticketInfoList.add(new TicketInfo(ticket.getId(), seatPriceInfo.seatId(), seatPriceInfo.price()));

            Payment payment = new Payment(ticket.getId(), seatPriceInfo.price(), PaymentStatus.PAID);
            paymentRepository.savePayment(payment);
        }

        return ticketInfoList;
    }
}
