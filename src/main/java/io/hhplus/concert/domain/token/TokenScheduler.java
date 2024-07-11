package io.hhplus.concert.domain.token;

import io.hhplus.concert.common.enums.TokenStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TokenScheduler {
    private final TokenRepository tokenRepository;

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void process() {
        List<Token> tokens = tokenRepository.findByStatusAndAvailableAtBefore(TokenStatusType.WAITING, LocalDateTime.now());
        tokens.forEach(token -> token.setStatus(TokenStatusType.AVAILABLE));
    }

    @Scheduled(fixedRate = 3, timeUnit = TimeUnit.MINUTES)
    public void expire() {
        List<Token> tokens = tokenRepository.findByStatusAndExpireAtBefore(TokenStatusType.AVAILABLE, LocalDateTime.now());
        tokens.forEach(token -> token.setStatus(TokenStatusType.EXPIRED));
    }
}
