package io.hhplus.concert.application.payment;

import io.hhplus.concert.domain.concert.dto.SeatPriceInfo;
import io.hhplus.concert.domain.payment.PaymentService;
import io.hhplus.concert.domain.payment.Ticket;
import io.hhplus.concert.domain.payment.dto.PaymentHistoryCommand;
import io.hhplus.concert.domain.payment.event.PaymentEventPublisher;
import io.hhplus.concert.domain.payment.event.PaymentSuccessEvent;
import io.hhplus.concert.domain.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserPaymentComponent {

    private final UserService userService;
    private final PaymentService paymentService;

    private final PaymentEventPublisher paymentEventPublisher;

    @Transactional
    public List<Ticket> updatePaymentData(Long userId, List<SeatPriceInfo> priceInfos) {
        Integer totalPrice = 0;
        for (SeatPriceInfo seatPriceInfo : priceInfos) totalPrice += seatPriceInfo.price();

        userService.consumeBalance(userId, totalPrice);
        List<Ticket> tickets = paymentService.billing(userId, priceInfos);

        // 히스토리 생성을 위한 Command 리스트 생성
        List<PaymentHistoryCommand> paymentHistoryCommands =
                tickets.stream()
                        .map(PaymentHistoryCommand::fromDomain)
                        .toList();

        PaymentSuccessEvent event = new PaymentSuccessEvent(paymentHistoryCommands);

        // 아웃박스에 status=init 상태로 insert
        paymentService.initOutboxMessage(PaymentSuccessEvent.topic, event, event.getIdentifier());

        // 결제 후 티켓 생성 성공하면 결제 성공 이벤트를 발행
        paymentEventPublisher.success(event);

        return tickets;
    }
}
