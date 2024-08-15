package io.hhplus.concert.application.payment;

import io.hhplus.concert.common.util.ObjectStringConverter;
import io.hhplus.concert.domain.concert.ConcertService;
import io.hhplus.concert.domain.concert.dto.SeatPriceInfo;
import io.hhplus.concert.domain.concert.model.Reservation;
import io.hhplus.concert.domain.payment.PaymentOutbox;
import io.hhplus.concert.domain.payment.PaymentService;
import io.hhplus.concert.domain.payment.Ticket;
import io.hhplus.concert.domain.payment.dto.TicketInfo;
import io.hhplus.concert.domain.payment.event.PaymentEventPublisher;
import io.hhplus.concert.domain.payment.event.PaymentSuccessEvent;
import io.hhplus.concert.domain.token.Token;
import io.hhplus.concert.domain.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserPaymentFacade implements UserPaymentService {
    private final TokenService tokenService;
    private final ConcertService concertService;
    private final UserPaymentComponent userPaymentComponent;
    private final PaymentService paymentService;
    private final PaymentEventPublisher paymentEventPublisher;

    @Override
    public List<TicketInfo> billing(String authorization) {
        Token token = tokenService.findActiveToken(authorization);
        List<Reservation> reservations = concertService.findUserReservations(token.getUserId());

        if (reservations.isEmpty()) {
            log.info("[결제API][유저ID : {}] 결제할 수 있는 예약 좌석 없음", token.getUserId());
            return List.of();
        }

        List<SeatPriceInfo> priceInfos = concertService.findReservationPrice(
                reservations.stream()
                        .map(Reservation::getSeatId)
                        .toList());

        // 결제와 관련되어 Transaction으로 묶여야하는 메서드 3개를 하나의 컴포넌트로 묶음
        List<Ticket> tickets = userPaymentComponent.updatePaymentData(token.getUserId(), priceInfos);

        // 결제 완료 시 토큰 만료
        tokenService.deleteActiveToken(authorization);

        return tickets
                .stream()
                .map(ticket -> new TicketInfo(ticket.getId(), ticket.getSeatId(), ticket.getPrice()))
                .toList();
    }

    @Override
    public void retryInitMessages() {
        List<PaymentOutbox> outboxes =
                paymentService.findInitOutboxMessages().stream()
                        .filter(PaymentOutbox::isRetryable)
                        .toList();

        if (outboxes.isEmpty()) return;

        for (PaymentOutbox outbox : outboxes) {
            // 지금은 결제 관련 카프카 메시지 토픽이 하나밖에 없어서 간단하게 if문으로 처리함
            if(outbox.getTopic().equals(PaymentSuccessEvent.topic)) {
                PaymentSuccessEvent event = ObjectStringConverter.fromJson(outbox.getMessage(), PaymentSuccessEvent.class);
                paymentEventPublisher.success(event);
            }
        }
    }

    @Override
    public void markOutboxAsPublished(String identifier) {
        paymentService.markOutboxAsPublished(identifier);

    }
}
