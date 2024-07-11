package io.hhplus.concert.infrastructure.token;

import io.hhplus.concert.common.enums.TokenStatusType;
import io.hhplus.concert.domain.token.Token;
import io.hhplus.concert.domain.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {

    private final TokenJpaRepository tokenJpaRepository;

    @Override
    public Optional<Token> findByToken(String token) {
        return tokenJpaRepository.findByToken(token);
    }

    @Override
    public List<Token> findByStatus(TokenStatusType status) {
        return tokenJpaRepository.findByStatus(status);
    }

    @Override
    public List<Token> findByStatusAndExpireAtBefore(TokenStatusType status, LocalDateTime expireAt) {
        return tokenJpaRepository.findByStatusAndExpireAtBefore(status, expireAt);
    }

    @Override
    public List<Token> findByStatusAndAvailableAtBefore(TokenStatusType status, LocalDateTime availableAt) {
        return tokenJpaRepository.findByStatusAndAvailableAtBefore(status, availableAt);
    }

    @Override
    public Optional<Long> findFirstPositionId() {
        return tokenJpaRepository.findFirstPositionId();
    }

    @Override
    public Token save(Token token) {
        return tokenJpaRepository.saveAndFlush(token);
    }

    @Override
    public List<Token> findAll() {
        return tokenJpaRepository.findAll();
    }

    @Override
    public List<Token> saveAll(List<Token> tokens) {
        return tokenJpaRepository.saveAll(tokens);
    }


}
