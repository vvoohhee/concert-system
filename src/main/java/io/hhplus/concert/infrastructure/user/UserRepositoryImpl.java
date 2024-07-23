package io.hhplus.concert.infrastructure.user;

import io.hhplus.concert.domain.user.Balance;
import io.hhplus.concert.domain.user.BalanceHistory;
import io.hhplus.concert.domain.user.User;
import io.hhplus.concert.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final BalanceJpaRepository repository;
    private final BalanceHistoryJpaRepository historyRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findUserById(Long id) {
        return userJpaRepository.findById(id);
    }

    @Override
    public Optional<Balance> findBalanceByUserId(Long id) {
        return repository.findByUserId(id);
    }

    @Override
    public User saveUser(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public Balance saveBalance(Balance balance) {
        return repository.save(balance);
    }

    @Override
    public BalanceHistory saveHistory(BalanceHistory balanceHistory) {
        return historyRepository.save(balanceHistory);
    }
}
