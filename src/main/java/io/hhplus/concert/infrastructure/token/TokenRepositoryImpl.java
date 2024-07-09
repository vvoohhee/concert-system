package io.hhplus.concert.infrastructure.token;

import io.hhplus.concert.domain.token.Token;
import io.hhplus.concert.domain.token.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {

    private final TokenEntityMapper mapper;
    private final TokenJpaRepository tokenJpaRepository;

    @Override
    public Token findByToken(String token) {
        return mapper.toDomain(
                tokenJpaRepository.findByToken(token)
        );
    }

    @Override
    public Token save(Token token) {
        return mapper.toDomain(
                tokenJpaRepository.save(mapper.toEntity(token))
        );
    }

    @Override
    public Optional<Long> findFirstPositionIdOrderByIdDesc() {
        return tokenJpaRepository.findFirstPositionIdOrderByIdDesc();
    }

    @Override
    public List<Token> findAll() {
        return tokenJpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Token> saveAll(List<Token> tokens) {
        List<TokenEntity> entities = tokens.stream().map(mapper::toEntity).toList();
        return tokenJpaRepository.saveAll(entities).stream().map(mapper::toDomain).toList();
    }


}
