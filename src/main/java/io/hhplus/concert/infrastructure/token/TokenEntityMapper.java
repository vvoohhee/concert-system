package io.hhplus.concert.infrastructure.token;

import io.hhplus.concert.domain.token.Token;
import org.springframework.stereotype.Component;

@Component
public class TokenEntityMapper {
    public TokenEntity toEntity(Token token) {
        return TokenEntity.builder()
                .id(token.getId())
                .userId(token.getUserId())
                .token(token.getToken())
                .status(token.getStatus())
                .createdAt(token.getCreatedAt())
                .expiresAt(token.getExpiresAt())
                .build();
    }

    public Token toDomain(TokenEntity entity) {
        return new Token(entity.getId(),
                entity.getUserId(),
                entity.getToken(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getExpiresAt());
    }
}
