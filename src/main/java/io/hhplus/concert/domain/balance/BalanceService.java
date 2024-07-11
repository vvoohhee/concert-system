package io.hhplus.concert.domain.balance;

import io.hhplus.concert.common.exception.NotFoundException;
import io.hhplus.concert.domain.balance.command.FindBalanceResponse;
import io.hhplus.concert.domain.balance.command.RechargeRequestCommand;
import io.hhplus.concert.domain.balance.command.RechargeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final BalanceRepository balanceRepository;

    public FindBalanceResponse find(Long userId) {
        Balance balance = balanceRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("회원의 잔액 정보 없음"));

        return new FindBalanceResponse(balance.getBalance(), balance.getCreatedAt(), balance.getUpdatedAt());
    }

    public RechargeResponse recharge(RechargeRequestCommand command) {
        Balance balance = balanceRepository.findByUserId(command.userId())
                .orElseThrow(() -> new NotFoundException("회원의 잔액 정보 없음"));

        balance.recharge(command.amount());
        balanceRepository.save(balance);

        return new RechargeResponse(balance.getId(), balance.getBalance());
    }

    public void consume(Long userId, Integer totalPrice) {
        Balance balance = balanceRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("회원의 잔액 정보 없음"));

        balance.consume(totalPrice);
    }
}
