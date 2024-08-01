package io.hhplus.concert.domain.token;

import io.hhplus.concert.common.enums.ErrorCode;
import io.hhplus.concert.common.enums.TokenStatusType;
import io.hhplus.concert.common.exception.CustomException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private TokenStatusType status;

    @Column(name = "created_at",
            nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "available_at")
    private LocalDateTime availableAt;

    @Column(name = "expire_at")
    private LocalDateTime expireAt;

    @Column(name = "last_request_at")
    private LocalDateTime lastRequestAt;

    // 사용자의 대기 순번
    @Transient
    private Long position;

    // 한 번에 ACTIVE 상태로 바꿀 수 있는 개수
    public final static int ACTIVATABLE_COUNT_PER_MIN = 50;

    // ACTIVE 토큰의 최대 개수
    public final static int MAX_ACTIVE_LIMIT = 400;

    // 사용자의 처리 기간
    public final static long TTL_MIN = 10;

    private void validateId(Long id) {
        if (Objects.isNull(id)) throw new CustomException(ErrorCode.ILLEGAL_ARGUMENT);
    }

    // 토큰 생성 요청 시
    public Token(Long userId) {
        validateId(userId);

        this.userId = userId;
        this.token = UUID.randomUUID().toString();
        this.status = TokenStatusType.WAITING;
        this.createdAt = LocalDateTime.now();
    }

    public Token(String token, Long userId) {
        validateId(userId);
        this.token = token;
        this.userId = userId;
    }

    // 대기중인 토큰의 대기 순서를 계산
    public void setPosition(Long id, Long first) {
        if (id > first) {
            position = id - first + 1;
        } else {
            position = 1L;
        }
    }

    public void setAvailableAtAndExpireAt() {
        if (Objects.isNull(position)) throw new CustomException(ErrorCode.TOKEN_NOT_EXIST);

        // 현재 시간 + (내 순서 / 처리 시간당 입장 가능한 사람 수) * 놀이기구 한 번당 처리 시간
        availableAt = LocalDateTime.now().plusMinutes((position / ACTIVATABLE_COUNT_PER_MIN) * TTL_MIN);
        expireAt = this.availableAt.plusMinutes(TTL_MIN);
    }

    public void updateLastRequestAt(LocalDateTime lastRequestAt) {
        this.lastRequestAt = lastRequestAt;
    }
}
