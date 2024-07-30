package io.hhplus.concert.domain.user;

import io.hhplus.concert.common.enums.ErrorCode;
import io.hhplus.concert.common.exception.CustomException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "balance")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "balance", nullable = false)
    private Integer balance;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    private int version;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    public Balance(Long userId) {
        if (Objects.isNull(userId)) throw new CustomException(ErrorCode.ILLEGAL_ARGUMENT);

        this.userId = userId;
        this.balance = 0;
    }

    public Balance(Long userId, Integer balance) {
        if (Objects.isNull(userId)) {
            throw new IllegalArgumentException("잘못된 사용자에 대한 요청");
        }
        if (Objects.isNull(balance) || balance < 0) {
            throw new IllegalArgumentException("잘못된 잔액 값에 대한 요청");
        }
        this.userId = userId;
        this.balance = balance;
    }

    public void recharge(Integer amount) {
        if (Objects.isNull(amount) || amount < 0) throw new CustomException(ErrorCode.ILLEGAL_ARGUMENT);

        balance += amount;
    }

    public void consume(Integer amount) {
        if (Objects.isNull(amount) || amount < 0) throw new CustomException(ErrorCode.ILLEGAL_ARGUMENT);

        balance -= amount;

        if (balance < 0) throw new CustomException(ErrorCode.ILLEGAL_PAYMENT_ARGUMENT);
    }
}
