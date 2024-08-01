package io.hhplus.concert.application.payment;

import io.hhplus.concert.domain.user.UserService;
import io.hhplus.concert.domain.concert.ConcertService;
import io.hhplus.concert.domain.concert.dto.SeatPriceInfo;
import io.hhplus.concert.domain.concert.model.Reservation;
import io.hhplus.concert.domain.payment.PaymentService;
import io.hhplus.concert.domain.payment.Ticket;
import io.hhplus.concert.domain.payment.dto.TicketInfo;
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
    private final UserService userService;
    private final PaymentService paymentService;

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

        return tickets
                .stream()
                .map(ticket -> new TicketInfo(ticket.getId(), ticket.getSeatId(), ticket.getPrice()))
                .toList();

    }
}
