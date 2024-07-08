package io.hhplus.concert.domain.balance;

import io.hhplus.concert.common.enums.BalanceHistoryType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
public class BalanceHistory {
    private Long id;
    private Long balanceId;
    private Integer amount;
    private BalanceHistoryType type;
    private LocalDateTime createdAt;

    public BalanceHistory(Long balanceId, Integer amount, BalanceHistoryType type) {
        if(Objects.isNull(balanceId)) {
            throw new IllegalArgumentException("잘못된 히스토리 생성 요청");
        }

        if(Objects.isNull(amount) || amount <= 0) {
            throw new IllegalArgumentException("잘못된 금액 요청");
        }

        this.balanceId = balanceId;
        this.amount = amount;
        this.type = type;
    }
}
