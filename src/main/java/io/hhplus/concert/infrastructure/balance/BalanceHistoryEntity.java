package io.hhplus.concert.infrastructure.balance;

import io.hhplus.concert.common.enums.BalanceHistoryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="balance_history")
public class BalanceHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="balance_id", nullable = false)
    private Long balanceId;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private BalanceHistoryType type;

    @Column(name="created_at",
            nullable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
