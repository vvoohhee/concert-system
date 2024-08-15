package io.hhplus.concert.interfaces.scheduler;

import io.hhplus.concert.application.payment.UserPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class PaymentOutboxScheduler {

    private final UserPaymentService userPaymentService;

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES)
    public void retryInitMessages() {
        userPaymentService.retryInitMessages();
    }
}
