package io.hhplus.concert.domain.balance;

import io.hhplus.concert.common.enums.ErrorCode;
import io.hhplus.concert.common.exception.CustomException;
import io.hhplus.concert.domain.balance.dto.RechargeCommand;
import io.hhplus.concert.domain.balance.dto.SaveBalanceHistoryCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final BalanceRepository balanceRepository;

    public Balance find(Long userId) {
        Balance balance = balanceRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_DATA));

        return balance;
    }

    public Balance recharge(RechargeCommand command) {
        Balance balance = balanceRepository.findByUserId(command.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.NO_DATA));

        balance.recharge(command.amount());
        balanceRepository.save(balance);

        return balance;
    }

    public void consume(Long userId, Integer totalPrice) {
        Balance balance = balanceRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_DATA));

        balance.consume(totalPrice);
    }

    public BalanceHistory saveHistory(SaveBalanceHistoryCommand command) {
        return balanceRepository.saveHistory(new BalanceHistory(command.balanceId(), command.amount(), command.type()));
    }
}
