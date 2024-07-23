package io.hhplus.concert.domain.user;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findUserById(Long id);
    Optional<Balance> findBalanceByUserId(Long id);
    User saveUser(User user);
    Balance saveBalance(Balance balance);
    BalanceHistory saveHistory(BalanceHistory balanceHistory);
}
