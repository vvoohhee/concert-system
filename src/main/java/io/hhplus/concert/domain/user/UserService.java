package io.hhplus.concert.domain.user;

import io.hhplus.concert.common.enums.ErrorCode;
import io.hhplus.concert.common.exception.CustomException;
import io.hhplus.concert.domain.user.dto.RechargeCommand;
import io.hhplus.concert.domain.user.dto.SaveBalanceHistoryCommand;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Balance findBalance(Long userId) {
        Balance balance = userRepository.findBalanceByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_DATA));

        return balance;
    }

    @Transactional
    public Balance rechargeBalance(RechargeCommand command) {
        Balance balance = userRepository.findBalanceByUserId(command.userId())
                .orElseThrow(() -> new CustomException(ErrorCode.NO_DATA));

        balance.recharge(command.amount());
        return balance;
    }

    public void consumeBalance(Long userId, Integer totalPrice) {
        Balance balance = userRepository.findBalanceByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_DATA));

        balance.consume(totalPrice);
    }

    public BalanceHistory saveHistory(SaveBalanceHistoryCommand command) {
        return userRepository.saveHistory(new BalanceHistory(command.balanceId(), command.amount(), command.type()));
    }

    public Optional<User> findUser(Long userId) {
        return userRepository.findUserById(userId);
    }
}
