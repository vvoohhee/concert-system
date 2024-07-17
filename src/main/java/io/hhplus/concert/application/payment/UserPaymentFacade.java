package io.hhplus.concert.application.payment;

import io.hhplus.concert.common.exception.CustomException;
import io.hhplus.concert.domain.balance.BalanceService;
import io.hhplus.concert.domain.balance.command.FindBalanceResponse;
import io.hhplus.concert.domain.concert.ConcertService;
import io.hhplus.concert.domain.concert.dto.SeatPriceInfo;
import io.hhplus.concert.domain.payment.PaymentService;
import io.hhplus.concert.domain.payment.dto.TicketInfo;
import io.hhplus.concert.domain.token.Token;
import io.hhplus.concert.domain.token.TokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserPaymentFacade implements UserPaymentService {
    private final TokenService tokenService;
    private final ConcertService concertService;
    private final BalanceService balanceService;
    private final PaymentService paymentService;

    @Override
    @Transactional
    public List<TicketInfo> billing(String authorization) {
        Token token = tokenService.find(authorization);
        List<SeatPriceInfo> reservedSeats = concertService.findReservedPrice(token.getUserId());

        if (reservedSeats.isEmpty()) return List.of();

        FindBalanceResponse userBalance = balanceService.find(token.getUserId());

        Integer totalPrice = 0;
        for (SeatPriceInfo seatPriceInfo : reservedSeats) {
            totalPrice += seatPriceInfo.price();
        }

        if (userBalance == null || userBalance.balance() < totalPrice) {
            throw new CustomException("잔액이 충분하지 않습니다.");
        }

        balanceService.consume(token.getUserId(), totalPrice);
        return paymentService.billing(token.getUserId(), reservedSeats);
    }
}
