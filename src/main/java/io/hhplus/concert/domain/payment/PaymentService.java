package io.hhplus.concert.domain.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.concert.domain.concert.dto.SeatPriceInfo;
import io.hhplus.concert.domain.payment.dto.PaymentHistoryCommand;
import io.hhplus.concert.domain.payment.event.PaymentEventPublisher;
import io.hhplus.concert.domain.payment.event.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher paymentEventPublisher;

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

        // 결제 후 티켓 생성 성공하면 결제 성공 이벤트를 발행
        List<PaymentHistoryCommand> events =
                tickets.stream()
                        .map(ticket -> new PaymentHistoryCommand(ticket.getId(), ticket.getPrice()))
                        .toList();

        paymentEventPublisher.success(new PaymentSuccessEvent(events));

        return tickets;
    }

    public void savePaymentHistories(List<PaymentHistoryCommand> command) {

        List<Payment> payments = command
                .stream()
                .map(c -> new Payment(c.getTicketId(), c.getPrice(), c.getStatus()))
                .toList();

        try {
            paymentRepository.savePayments(payments);
        } catch (Exception e) {
            ObjectMapper objectMapper = new ObjectMapper();
            log.error("[결제API][결제히스토리생성] 결제 정보를 DB에 저장하는 과정에서 알 수 없는 에러 발생");
            try {
                // 실패한 데이터를 로그로 확인할 수 있도록 JSON String으로 변경 후 로그에 기록
                log.error("[결제API][결제히스토리생성] 요청 데이터 : {}", objectMapper.writeValueAsString(payments));
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
