package io.hhplus.concert.application.user;

import io.hhplus.concert.common.enums.BalanceHistoryType;
import io.hhplus.concert.domain.user.Balance;
import io.hhplus.concert.domain.user.UserService;
import io.hhplus.concert.domain.user.dto.BalanceInfo;
import io.hhplus.concert.domain.user.dto.RechargeCommand;
import io.hhplus.concert.domain.user.dto.SaveBalanceHistoryCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserBalanceFacade implements UserBalanceService {

    private final UserService userService;

    @Override
    public BalanceInfo findUserBalance(Long userId) {
        Balance balance = userService.findBalance(userId);
        return new BalanceInfo(balance.getUserId(), balance.getBalance());
    }

    @Override
    public BalanceInfo rechargeBalance(Long userId, Integer amount) {
        Balance balance = userService.rechargeBalance(new RechargeCommand(userId, amount));

        SaveBalanceHistoryCommand saveHistoryRequest = new SaveBalanceHistoryCommand(balance.getId(), amount, BalanceHistoryType.RECHARGE);
        userService.saveHistory(saveHistoryRequest);

        return new BalanceInfo(balance.getUserId(), balance.getBalance());
    }
}
