package io.hhplus.concert.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BalanceUnitTest {
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("유저의 잔액이 0보다 작을 때 예외처리 테스트")
    void balanceLessThanZeroTest() {
        Long userId = 1L;
        Integer balance = -10;
        assertThrows(RuntimeException.class, () -> new Balance(userId, balance));
    }

    @Test
    @DisplayName("잔액 충전 테스트")
    void rechargeTest() {
        Long userId = 1L;
        Integer original = 1000;
        Integer rechargeAmount = 500;

        Balance balance = new Balance(userId, original);
        balance.recharge(rechargeAmount);
        assertEquals(original + rechargeAmount, balance.getBalance());
    }

    @Test
    @DisplayName("잔액 사용 테스트")
    void consumeTest() {
        Long userId = 1L;
        Integer original = 1000;
        Integer consumeAmount = 500;

        Balance balance = new Balance(userId, original);
        balance.consume(consumeAmount);
        assertEquals(original + consumeAmount, balance.getBalance());
    }

}
