package io.hhplus.concert.application.payment;

import io.hhplus.concert.domain.concert.dto.SeatPriceInfo;
import io.hhplus.concert.domain.payment.PaymentService;
import io.hhplus.concert.domain.payment.Ticket;
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

    @Transactional
    public List<Ticket> updatePaymentData(Long userId, List<SeatPriceInfo> priceInfos) {
        Integer totalPrice = 0;
        for (SeatPriceInfo seatPriceInfo : priceInfos) totalPrice += seatPriceInfo.price();

        userService.consumeBalance(userId, totalPrice);
        return paymentService.billing(userId, priceInfos);
    }
}
