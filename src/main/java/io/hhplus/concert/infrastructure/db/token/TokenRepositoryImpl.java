package io.hhplus.concert.infrastructure.db.token;

import io.hhplus.concert.common.enums.TokenStatusType;
import io.hhplus.concert.domain.token.Token;
import io.hhplus.concert.domain.token.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {
    private final TokenRedisRepository repository;

    @Override
    public Boolean issueWaitingToken(Token token) {
        return repository.issueWaitingToken(token);
    }

    @Override
    @Transactional
    public void issueActiveTokens(List<Token> tokens) {
        // 두 메서드를 하나의 트랜잭션으로 묶어서 요청
        repository.issueActiveTokens(tokens);
        repository.deleteWaitingToken(tokens.size());
    }

    @Override
    public Long findRank(String token) {
        return repository.findRank(token);
    }

    @Override
    public List<String> findAvailableWaitingTokens(int maxAvailableCount) {
        return repository.findAvailableWaitingTokens(maxAvailableCount);
    }

    @Override
    public void deleteActivatedWaitingToken(int count) {
        repository.deleteWaitingToken(count);
    }

    @Override
    public Boolean deleteActiveToken(String token) {
        return repository.expireActiveToken(token);
    }

    @Override
    public Boolean deleteTokenUserData(String token) {
        return repository.deleteTokenUserData(token);
    }

    @Override
    public Optional<Token> findWaitingTokenByToken(String token) {
        return repository.findWaitingTokenByToken(token);
    }

    @Override
    public Optional<Token> findActiveTokenByToken(String token) {
        return repository.findActiveTokenByToken(token);
    }

    @Override
    public Integer findActiveTokenCount() {
        return repository.findActiveTokenCount();
    }

    @Override
    public List<Token> findByStatusAndExpireAtBefore(TokenStatusType status, LocalDateTime expireAt) {
        return List.of();
    }

    @Override
    public Optional<Long> findFirstPositionId() {
        return Optional.empty();
    }
}
