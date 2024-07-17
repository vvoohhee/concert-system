package io.hhplus.concert.application.balance;

import io.hhplus.concert.common.enums.BalanceHistoryType;
import io.hhplus.concert.domain.balance.BalanceHistoryService;
import io.hhplus.concert.domain.balance.BalanceService;
import io.hhplus.concert.domain.balance.command.RechargeRequestCommand;
import io.hhplus.concert.domain.balance.command.RechargeResponse;
import io.hhplus.concert.domain.balance.command.SaveBalanceHistoryRequestCommand;
import io.hhplus.concert.interfaces.presentation.balance.dto.FindBalanceDto;
import io.hhplus.concert.interfaces.presentation.balance.dto.RechargeBalanceDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserBalanceFacade implements UserBalanceService {

    private final BalanceService balanceService;
    private final BalanceHistoryService balanceHistoryService;

    @Override
    public FindBalanceDto.Response findUserBalance(Long userId) {
        return FindBalanceDto.of(balanceService.find(userId));
    }

    @Override
    @Transactional
    public RechargeBalanceDto.Response recharge(Long userId, Integer amount) {
        RechargeResponse rechargeResult = balanceService.recharge(new RechargeRequestCommand(userId, amount));

        SaveBalanceHistoryRequestCommand saveHistoryRequest = new SaveBalanceHistoryRequestCommand(rechargeResult.id(), amount, BalanceHistoryType.RECHARGE);
        balanceHistoryService.save(saveHistoryRequest);

        return RechargeBalanceDto.of(rechargeResult);
    }
}
