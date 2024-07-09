package io.hhplus.concert.domain.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public Token issue(Long userId) {
        Token token = new Token(userId);
        return tokenRepository.save(token);
    }

    public Token find(String tokenString) {
        Token token = tokenRepository.findByToken(tokenString);
        Long first = tokenRepository.findFirstPositionIdOrderByIdDesc().orElse(0L);
        token.setPosition(token.getId(), first);
        return token;
    }

    // 만료된 토큰을 어떻게 처리할 것인가?
    public void expire() {
        List<Token> tokens = tokenRepository.findAll();
        for (Token token : tokens) token.expire();

        tokenRepository.saveAll(tokens);
    }
}
