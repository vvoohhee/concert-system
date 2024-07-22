package io.hhplus.concert.application.balance;

import io.hhplus.concert.common.enums.BalanceHistoryType;
import io.hhplus.concert.domain.balance.Balance;
import io.hhplus.concert.domain.balance.BalanceService;
import io.hhplus.concert.domain.balance.dto.BalanceInfo;
import io.hhplus.concert.domain.balance.dto.RechargeCommand;
import io.hhplus.concert.domain.balance.dto.SaveBalanceHistoryCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserBalanceFacade implements UserBalanceService {

    private final BalanceService balanceService;

    @Override
    public BalanceInfo findUserBalance(Long userId) {
        Balance balance = balanceService.find(userId);
        return new BalanceInfo(balance.getUserId(), balance.getBalance());
    }

    @Override
    @Transactional
    public BalanceInfo recharge(Long userId, Integer amount) {
        Balance balance = balanceService.recharge(new RechargeCommand(userId, amount));

        SaveBalanceHistoryCommand saveHistoryRequest = new SaveBalanceHistoryCommand(balance.getId(), amount, BalanceHistoryType.RECHARGE);
        balanceService.saveHistory(saveHistoryRequest);

        return new BalanceInfo(balance.getUserId(), balance.getBalance());
    }
}
