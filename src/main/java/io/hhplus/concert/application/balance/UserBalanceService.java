package io.hhplus.concert.application.balance;

import io.hhplus.concert.domain.balance.dto.BalanceInfo;

public interface UserBalanceService {
    BalanceInfo findUserBalance(Long userId);
    BalanceInfo recharge(Long userId, Integer amount);
}
