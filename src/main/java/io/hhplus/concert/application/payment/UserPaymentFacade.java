package io.hhplus.concert.application.payment;

import io.hhplus.concert.common.enums.ErrorCode;
import io.hhplus.concert.common.exception.CustomException;
import io.hhplus.concert.domain.balance.Balance;
import io.hhplus.concert.domain.balance.BalanceService;
import io.hhplus.concert.domain.concert.ConcertService;
import io.hhplus.concert.domain.concert.dto.SeatPriceInfo;
import io.hhplus.concert.domain.concert.model.Reservation;
import io.hhplus.concert.domain.payment.PaymentService;
import io.hhplus.concert.domain.payment.Ticket;
import io.hhplus.concert.domain.payment.dto.TicketInfo;
import io.hhplus.concert.domain.token.Token;
import io.hhplus.concert.domain.token.TokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserPaymentFacade implements UserPaymentService {
    private final TokenService tokenService;
    private final ConcertService concertService;
    private final BalanceService balanceService;
    private final PaymentService paymentService;

    @Override
    @Transactional
    public List<TicketInfo> billing(String authorization) {
        // 토큰으로 사용자 정보 조회
        Token token = tokenService.find(authorization);

        // 사용자 ID로 예약된 예약 건들을 조회
        List<Reservation> reservations = concertService.findUserReservations(token.getUserId());

        if (reservations.isEmpty()) {
            log.info("[결제API][유저ID : {}] 결제할 수 있는 예약 좌석 없음", token.getUserId());
            return List.of();
        }

        // 예약된 좌석들의 가격 정보를 조회
        List<SeatPriceInfo> priceInfos = concertService.findReservationPrice(
                reservations.stream()
                        .map(Reservation::getSeatId)
                        .toList());

        // 사용자가 결제할 총 금액 계산
        Integer totalPrice = 0;
        for (SeatPriceInfo seatPriceInfo : priceInfos) totalPrice += seatPriceInfo.price();

        // 사용자의 잔액 조회
        Balance userBalance = balanceService.find(token.getUserId());


        // 잔액 예외처리
        if (Objects.isNull(userBalance)) throw new CustomException(ErrorCode.BALANCE_NOT_EXIST);

        // 사용자 잔액 사용
        balanceService.consume(token.getUserId(), totalPrice);

        // 좌석별 티켓 생성
        List<Ticket> tickets = paymentService.billing(token.getUserId(), priceInfos);

        // 결제 히스토리 생성
        paymentService.savePaymentHistories(tickets);

        return tickets
                .stream()
                .map(ticket -> new TicketInfo(ticket.getId(), ticket.getSeatId(), ticket.getPrice()))
                .toList();

    }
}
