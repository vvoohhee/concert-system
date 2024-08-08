package io.hhplus.concert.concurrency;

import io.hhplus.concert.application.payment.UserPaymentFacade;
import io.hhplus.concert.application.token.UserTokenFacade;
import io.hhplus.concert.application.user.UserBalanceService;
import io.hhplus.concert.domain.token.TokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@SpringBootTest
@Slf4j
public class PaymentConcurrencyTest {

    @Autowired
    private UserPaymentFacade userPaymentService;

    @Autowired
    private UserTokenFacade userTokenFacade;

    @Autowired
    private UserBalanceService userBalanceService;

    @Test
    @DisplayName("한 유저가 결제 요청을 동시에 하는 경우")
    public void 결제_동시성_테스트() throws InterruptedException {
        // 테스트를 위한 데이터 생성

        Long userId = 1L;
        TokenInfo token = userTokenFacade.issueWaitingToken(userId);

        // 동시성 테스트 시작 - 시작 시간 기록
        Long startTime = System.currentTimeMillis();

        CountDownLatch latch = new CountDownLatch(10);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        AtomicInteger count = new AtomicInteger(0);

        IntStream.range(0, 10).forEach(i -> {
            executor.submit(() -> {
                try {
                    log.info("결제 요청 요청 - 스레드 : {}", Thread.currentThread().getName());
                    userPaymentService.billing(token.token());
                    log.info("결제 요청 성공 : {}", Thread.currentThread().getName());
                    count.getAndAdd(1);
                } catch (Exception e) {
                    log.info("{} - 스레드 : {}", e.getMessage(), Thread.currentThread().getName());
                } finally {
                    latch.countDown();
                }
            });
        });

        latch.await();

        // 동시성 테스트 끝 - 결과 기록
        log.info("동시성 테스트 종료 - 성공 횟수 : {},  소요 시간 : {}ms", count, System.currentTimeMillis() - startTime);

        executor.shutdown();
    }

    @Test
    @DisplayName("결제 요청과 포인터 충전 요청이 동시에 들어오는 경우")
    public void 결제_충전_동시성_테스트() throws InterruptedException {
        // 테스트를 위한 데이터 생성
        Long userId = 1L;
        TokenInfo token = userTokenFacade.issueWaitingToken(userId);

        // 동시성 테스트 시작 - 시작 시간 기록
        Long startTime = System.currentTimeMillis();

        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> userPaymentService.billing(token.token())),
                CompletableFuture.runAsync(() -> userBalanceService.rechargeBalance(userId, 1000))
        );

        // 동시성 테스트 끝 - 결과 기록
        log.info("동시성 테스트 종료 - 소요 시간 : {}ms", System.currentTimeMillis() - startTime);
        log.info("사용자 잔액 : {}", userBalanceService.findUserBalance(userId));
    }
}
