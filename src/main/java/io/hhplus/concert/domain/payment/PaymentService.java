package io.hhplus.concert.domain.payment;

import io.hhplus.concert.common.enums.ErrorCode;
import io.hhplus.concert.common.enums.OutboxStatus;
import io.hhplus.concert.common.exception.CustomException;
import io.hhplus.concert.domain.concert.dto.SeatPriceInfo;
import jakarta.transaction.Transactional;
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

    public PaymentOutbox initOutboxMessage(String topic, Object message, String identifier) {
        PaymentOutbox outbox = new PaymentOutbox(topic, message, identifier);
        return paymentRepository.initOutbox(outbox);
    }

    public List<PaymentOutbox> findInitOutboxMessages() {
        return paymentRepository.findOutboxesByStatus(OutboxStatus.INIT);
    }

    @Transactional
    public void markOutboxAsPublished(String identifier) {
        PaymentOutbox outbox = paymentRepository.findOutboxByIdentifier(identifier);
        if(outbox == null) throw new CustomException(ErrorCode.NO_SUCH_MESSAGE);

        outbox.markAsPublished(); // 변경감지 때문에 save() 메서드 요청할 필요 없음
    }
}
