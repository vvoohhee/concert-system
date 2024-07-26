package io.hhplus.concert.concurrency;

import io.hhplus.concert.application.user.UserBalanceFacade;
import io.hhplus.concert.domain.user.Balance;
import io.hhplus.concert.domain.user.User;
import io.hhplus.concert.domain.user.UserRepository;
import io.hhplus.concert.domain.user.dto.BalanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@SpringBootTest
@Slf4j
public class BalanceConcurrencyTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBalanceFacade userBalanceFacade;

    @Test
    public void 잔액충전_테스트() throws InterruptedException {
        // 테스트를 위한 데이터 생성
        User u = userRepository.saveUser(new User());
        Balance b = userRepository.saveBalance(new Balance(u.getId(), 0));

        Long userId = u.getId();
        Integer amount = 300;

        CountDownLatch latch = new CountDownLatch(10);
        ExecutorService executor = Executors.newFixedThreadPool(10);

        AtomicInteger atomicInteger = new AtomicInteger(0);

        // 동시성 테스트 시작 - 시작 시간 기록
        Long startTime = System.currentTimeMillis();

        IntStream.range(0, 10).forEach(i -> {
            executor.submit(() -> {
                try {
                    log.info("잔액 업데이트 요청 - 스레드 : {}", Thread.currentThread().getName());

                    BalanceInfo balanceInfo = userBalanceFacade.rechargeBalance(userId, amount);
                    log.info("잔액 업데이트 성공 : {}, 업데이트 후 잔액 : {}", Thread.currentThread().getName(), balanceInfo.balance());
                    atomicInteger.getAndAdd(1);
                } catch (Exception e) {
                    log.info("{} - 스레드 : {}", e.getMessage(), Thread.currentThread().getName());
                } finally {
                    latch.countDown();
                }
            });
        });

        latch.await();

        // 동시성 테스트 끝 - 결과 기록
        BalanceInfo result = userBalanceFacade.findUserBalance(userId);
        log.info("동시성 테스트 종료 - 성공 횟수 : {}, 업데이트 후 잔액 : {}, 소요 시간 : {}ms", atomicInteger, result.balance(), System.currentTimeMillis() - startTime);

        executor.shutdown();
    }
}
