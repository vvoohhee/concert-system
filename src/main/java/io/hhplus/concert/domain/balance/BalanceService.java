package io.hhplus.concert.domain.balance;

import io.hhplus.concert.common.exception.NotFoundException;
import io.hhplus.concert.domain.balance.command.FindBalanceResponseCommand;
import io.hhplus.concert.domain.balance.command.RechargeRequestCommand;
import io.hhplus.concert.domain.balance.command.RechargeResponseCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final BalanceRepository balanceRepository;

    public FindBalanceResponseCommand find(Long userId) {
        Balance balance = balanceRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("회원의 잔액 정보 없음"));

        return new FindBalanceResponseCommand(balance.getBalance(), balance.getCreatedAt(), balance.getUpdatedAt());
    }

    public RechargeResponseCommand recharge(RechargeRequestCommand command) {
        Balance balance = balanceRepository.findByUserId(command.userId())
                .orElseThrow(() -> new NotFoundException("회원의 잔액 정보 없음"));

        balance.recharge(command.amount());
        balanceRepository.save(balance);

        return new RechargeResponseCommand(balance.getId(), balance.getBalance());
    }
}
