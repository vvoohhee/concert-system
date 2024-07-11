package io.hhplus.concert.domain.balance;

import io.hhplus.concert.common.exception.CustomException;
import io.hhplus.concert.common.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Balance {
    private Long id;
    private Long userId;
    private Integer balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Balance(Long userId) {
        this.userId = userId;
        this.balance = 0;
    }

    public Balance(Long userId, Integer balance) {
        if (Objects.isNull(userId)) {
            throw new NotFoundException("잘못된 사용자에 대한 요청");
        }
        if (Objects.isNull(balance) || balance < 0) {
            throw new NotFoundException("잘못된 잔액 값");
        }
        this.userId = userId;
        this.balance = balance;
    }

    public void recharge(Integer amount) {
        balance += amount;
    }

    public void consume(Integer amount) {
        balance -= amount;

        if(balance < 0) throw new CustomException("결제 시도 금액이 보유한 금액을 초과");

    }
}
