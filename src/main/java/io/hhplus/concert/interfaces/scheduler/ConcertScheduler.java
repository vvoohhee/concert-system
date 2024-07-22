package io.hhplus.concert.interfaces.scheduler;

import io.hhplus.concert.application.concert.UserConcertService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class ConcertScheduler {

    private final UserConcertService concertService;


    @Scheduled(fixedRate = 3, timeUnit = TimeUnit.MINUTES)
    public void resetReservation() {
        // 이선좌 상태인 좌석의 예약 시간이 5분 이상 경과한 경우,
        // 좌석의 상태 변경, Reservation 물리적 삭제\
        concertService.resetReservation();
    }

}
