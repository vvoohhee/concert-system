package io.hhplus.concert.domain.balance;

import io.hhplus.concert.common.enums.BalanceHistoryType;
import io.hhplus.concert.common.enums.ErrorCode;
import io.hhplus.concert.common.exception.CustomException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "balance_history")
@NoArgsConstructor
@Getter
@Setter
public class BalanceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "balance_id", nullable = false)
    private Long balanceId;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private BalanceHistoryType type;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public BalanceHistory(Long balanceId, Integer amount, BalanceHistoryType type) {
        if(Objects.isNull(balanceId)) {
            throw new CustomException(ErrorCode.ILLEGAL_ARGUMENT);
        }

        if(Objects.isNull(amount) || amount <= 0) {
            throw new CustomException(ErrorCode.ILLEGAL_ARGUMENT);
        }

        this.balanceId = balanceId;
        this.amount = amount;
        this.type = type;
        this.createdAt = LocalDateTime.now();
    }
}
