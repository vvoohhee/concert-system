package io.hhplus.concert.concurrency;

import io.hhplus.concert.application.concert.UserConcertFacade;
import io.hhplus.concert.application.token.UserTokenService;
import io.hhplus.concert.domain.concert.model.Concert;
import io.hhplus.concert.domain.concert.model.ConcertOption;
import io.hhplus.concert.domain.concert.model.Seat;
import io.hhplus.concert.domain.user.User;
import io.hhplus.concert.domain.user.UserRepository;
import io.hhplus.concert.infrastructure.concert.ConcertJpaRepository;
import io.hhplus.concert.infrastructure.concert.ConcertOptionJpaRepository;
import io.hhplus.concert.infrastructure.concert.SeatJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@SpringBootTest
@Slf4j
public class ConcertConcurrencyTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConcertJpaRepository concertJpaRepository;

    @Autowired
    private SeatJpaRepository seatJpaRepository;

    @Autowired
    private ConcertOptionJpaRepository concertOptionJpaRepository;

    @Autowired
    private UserConcertFacade userConcertService;

    @Autowired
    private UserTokenService userTokenService;

    @Test
    @DisplayName("좌석 예매가 동시에 들어오는 경우")
    public void 콘서트예매_동시성_테스트() throws InterruptedException {
        // 테스트를 위한 데이터 생성
        User user = userRepository.saveUser(new User());
        String token = userTokenService.issueWaitingToken(user.getId()).token();

        Concert concert = concertJpaRepository.save(new Concert("동시성테스트"));

        ConcertOption option = new ConcertOption(null, concert, 10000, 10, LocalDateTime.now(), LocalDateTime.now().plusDays(10));
        option = concertOptionJpaRepository.save(option);

        List<Long> seats = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            Seat seat = seatJpaRepository.save(new Seat(option.getId(), i));
            seats.add(seat.getId());
        }

        CountDownLatch latch = new CountDownLatch(50);
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // 동시성 테스트 시작 - 시작 시간 기록
        Long startTime = System.currentTimeMillis();

        AtomicInteger atomicInteger = new AtomicInteger(0);

        IntStream.range(0, 50).forEach(i -> {
            executor.submit(() -> {
                try {
                    log.info("좌석 예약 업데이트 요청 - 스레드 : {}", Thread.currentThread().getName());

                    List<Long> seatIdList = new ArrayList<>();
                    if(i%2 == 0) {
                        seatIdList.add(seats.get(0));
                        seatIdList.add(seats.get(1));
                        seatIdList.add(seats.get(5));
                        seatIdList.add(seats.get(6));
                        seatIdList.add(seats.get(7));
                        seatIdList.add(seats.get(8));
                    }else {
                        seatIdList.add(seats.get(1));
                        seatIdList.add(seats.get(0));
                        seatIdList.add(seats.get(2));
                        seatIdList.add(seats.get(3));
                        seatIdList.add(seats.get(4));
                    }
                    userConcertService.reserveSeats(seatIdList, token);
                    atomicInteger.getAndAdd(1);
                    log.info("좌석 예약 업데이트 성공 : {}", Thread.currentThread().getName());
                } catch (Exception e) {
//                    e.printStackTrace();
                    log.info("{} - 스레드 : {}", e.getMessage(), Thread.currentThread().getName());
                } finally {
                    latch.countDown();
                }
            });
        });

        latch.await();

        // 동시성 테스트 끝 - 결과 기록
        log.info("[ 동시성 테스트 종료 ]");
        log.info("성공 케이스 {}건, 총 소요 시간 : {}ms", atomicInteger.get(), System.currentTimeMillis() - startTime);

        executor.shutdown();
    }
}
