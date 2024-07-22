package io.hhplus.concert.domain.payment;

import io.hhplus.concert.common.enums.PaymentStatus;
import io.hhplus.concert.domain.concert.dto.SeatPriceInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public List<Ticket> billing(Long userId, List<SeatPriceInfo> priceInfoList) {
        List<Ticket> tickets = priceInfoList
                .stream()
                .map(info -> new Ticket(info.seatId(), userId, info.price()))
                .toList();

        try {
            tickets = paymentRepository.saveTickets(tickets);
        } catch (Exception e) {
            log.error("[결제API][유저ID : {}] 티켓 정보를 DB에 저장하는 과정에서 알 수 없는 에러 발생", userId);
        }

        return tickets;
    }

    public List<Payment> savePaymentHistories(List<Ticket> ticket) {

        List<Payment> payments = ticket
                .stream()
                .map(t -> new Payment(t.getId(), t.getPrice(), PaymentStatus.PAID))
                .toList();

        try {
            payments = paymentRepository.savePayments(payments);
        } catch (Exception e) {
            log.error("[결제API][유저ID : {}] 결제 정보를 DB에 저장하는 과정에서 알 수 없는 에러 발생", ticket.get(0).getUserId());
        }

        return payments;
    }
}
