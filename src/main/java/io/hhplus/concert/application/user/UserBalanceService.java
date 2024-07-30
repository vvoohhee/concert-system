package io.hhplus.concert.application.user;

import io.hhplus.concert.domain.user.dto.BalanceInfo;

public interface UserBalanceService {
    BalanceInfo findUserBalance(Long userId);
    BalanceInfo rechargeBalance(Long userId, Integer amount);
}
