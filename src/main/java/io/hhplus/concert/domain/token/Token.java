package io.hhplus.concert.domain.token;

import io.hhplus.concert.common.enums.TokenStatusType;
import io.hhplus.concert.common.exception.CustomException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
public class Token {
    private Long id;
    private Long userId;
    private String token;
    private TokenStatusType status;
    private Integer position;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    private final static int LIFETIME = 30; // Minute

    public Token(Long userId) {
        if (Objects.isNull(userId)) throw new IllegalArgumentException("유효하지 않은 유저에 대한 요청");

        this.userId = userId;
        this.token = UUID.randomUUID().toString();
        this.status = TokenStatusType.WAITING;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = this.createdAt.plusMinutes(LIFETIME);
    }

    public Token(Long id, Long userId, String token, TokenStatusType status, LocalDateTime createdAt, LocalDateTime expiresAt) {
        if (Objects.isNull(userId)) throw new IllegalArgumentException("유효하지 않은 유저에 대한 요청");
        if (expiresAt.isAfter(LocalDateTime.now()) || status.equals(TokenStatusType.EXPIRED)) {
            throw new CustomException("만료된 토큰");
        }

        this.id = id;
        this.userId = userId;
        this.token = token;
        this.status = status;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    void changeStatus(TokenStatusType status) {
        this.status = status;
    }

    void expire() {
        if (expiresAt.isAfter(LocalDateTime.now()) && status.equals(TokenStatusType.EXPIRED)) {
            this.status = TokenStatusType.EXPIRED;
        }
    }

    void process() {
        this.status = TokenStatusType.PENDING;
    }

    void done() {
        this.status = TokenStatusType.DONE;
    }

    public void setPosition(Long id, Long first) {
        if (id - first < 0) {
            this.position = 0;
        } else {
            this.position = Math.toIntExact(id - first);
        }
    }
}
