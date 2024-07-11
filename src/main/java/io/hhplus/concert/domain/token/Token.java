package io.hhplus.concert.domain.token;

import io.hhplus.concert.common.enums.TokenStatusType;
import io.hhplus.concert.common.exception.CustomException;
import io.hhplus.concert.common.exception.UnauthorizedException;
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
    private Integer position;

    // 처리할 수 있는 최대 사용자 수
    public final static int MAX_AVAILABLE_COUNT = 50;

    // 사용자의 처리 기간
    public final static int DURATION_MIN = 10;

    private void validateId(Long id) {
        if (Objects.isNull(id)) throw new IllegalArgumentException("유효하지 않은 ID");
    }

    // 토큰 생성 요청 시
    public Token(Long userId) {
        validateId(userId);

        this.userId = userId;
        this.token = UUID.randomUUID().toString();
        this.status = TokenStatusType.WAITING;
        this.createdAt = LocalDateTime.now();
    }

    // 대기중인 토큰의 대기 순서를 계산
    public void setPosition(Long id, Long first) {
        if (id > first) {
            position = Math.toIntExact(id - first) + 1;
        } else {
            position = 1;
        }
    }

    public void setAvailableAtAndExpireAt() {
        if (Objects.isNull(position)) throw new CustomException("대기 순번이 없는 유저");

        // 현재 시간 + (내 순서 / 처리 시간당 입장 가능한 사람 수) * 놀이기구 한 번당 처리 시간
        availableAt = LocalDateTime.now().plusMinutes((position / MAX_AVAILABLE_COUNT) * DURATION_MIN);
        expireAt = this.availableAt.plusMinutes(DURATION_MIN);
    }
}
