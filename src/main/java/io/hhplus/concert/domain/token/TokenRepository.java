package io.hhplus.concert.domain.token;

import java.util.List;
import java.util.Optional;

public interface TokenRepository {
    Token findByToken(String token);
    Token save(Token token);
    Optional<Long> findFirstPositionIdOrderByIdDesc();
    List<Token> findAll();
    List<Token> saveAll(List<Token> tokens);
}
