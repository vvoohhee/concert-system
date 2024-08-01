package io.hhplus.concert.interfaces.scheduler;

import io.hhplus.concert.application.token.UserTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenScheduler {
    private final UserTokenService tokenService;

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void activateToken() {
        tokenService.activateToken();
    }

//     @Scheduled(fixedRate = 3, timeUnit = TimeUnit.MINUTES)
//     이제 TTL을 설정해 Redis 토큰 만료(삭제) 처리가 이루어지기 때문에 스케줄러 필요 없음
    public void expire() {
        tokenService.expire();
    }
}
