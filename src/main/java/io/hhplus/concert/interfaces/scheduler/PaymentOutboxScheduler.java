package io.hhplus.concert.interfaces.scheduler;

import io.hhplus.concert.application.payment.UserPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class PaymentOutboxScheduler {

    private final UserPaymentService userPaymentService;

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    public void retryInitMessages() {
        userPaymentService.retryInitMessages();
    }
}
