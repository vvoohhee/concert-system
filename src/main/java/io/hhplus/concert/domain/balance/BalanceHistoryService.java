package io.hhplus.concert.domain.balance;

import io.hhplus.concert.domain.balance.command.SaveBalanceHistoryRequestCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BalanceHistoryService {
    private final BalanceHistoryRepository balanceHistoryRepository;

    public BalanceHistory save(SaveBalanceHistoryRequestCommand command) {
        return balanceHistoryRepository.save(new BalanceHistory(command.balanceId(), command.amount(), command.type()));
    }
}
