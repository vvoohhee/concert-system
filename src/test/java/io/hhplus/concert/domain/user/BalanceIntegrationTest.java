package io.hhplus.concert.domain.user;


import io.hhplus.concert.application.user.UserBalanceFacade;
import io.hhplus.concert.common.enums.ErrorCode;
import io.hhplus.concert.common.exception.CustomException;
import io.hhplus.concert.domain.user.dto.BalanceInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BalanceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserBalanceFacade userBalanceFacade;

    @Test
    public void 잔액조회_성공() {
        // given
        Long userId = 1L;

        // when
        Balance balance = userRepository.findBalanceByUserId(userId).orElse(null);
        BalanceInfo response = userBalanceFacade.findUserBalance(userId);

        // then
        assert balance != null;
        assertEquals(balance.getBalance(), response.balance());
    }

    @Test
    public void 잔액조회_실패_유저아이디_NULL() {
        // given
        Long userId = null;

        // when - then
        CustomException exception = assertThrows(CustomException.class, () -> userBalanceFacade.findUserBalance(userId));
        assertEquals(ErrorCode.NO_DATA.getMessage(), exception.getMessage());
    }

    @Test
    public void 잔액조회_실패_데이터_없음() {
        // given
        Long userId = 999999999L;

        // when - then
        CustomException exception = assertThrows(CustomException.class, () -> userBalanceFacade.findUserBalance(userId));
        assertEquals(ErrorCode.NO_DATA.getMessage(), exception.getMessage());
    }


    @Test
    public void 잔액충전_성공() {
        Long userId = 1L;
        Integer amount = 500;

        Balance balance = userRepository.findBalanceByUserId(userId).orElse(null);
        BalanceInfo response = userBalanceFacade.rechargeBalance(userId, amount);

        assert balance != null;
        assertEquals(balance.getBalance() + amount, response.balance());
    }

    @Test
    public void 잔액충전_실패_유저아이디_NULL() {
        Long userId = null;
        Integer amount = 500;

        CustomException exception = assertThrows(CustomException.class, () -> userBalanceFacade.rechargeBalance(userId, amount));
        assertEquals(ErrorCode.NO_DATA.getMessage(), exception.getMessage());
    }

    @Test
    public void 잔액충전_실패_충전금액_NULL() {
        Long userId = 1L;
        Integer amount = null;

        CustomException exception = assertThrows(CustomException.class, () -> userBalanceFacade.rechargeBalance(userId, amount));
        assertEquals(ErrorCode.ILLEGAL_ARGUMENT.getMessage(), exception.getMessage());
    }

    @Test
    public void 잔액충전_실패_충전금액_음수() {
        Long userId = 1L;
        Integer amount = -500;

        CustomException exception = assertThrows(CustomException.class, () -> userBalanceFacade.rechargeBalance(userId, amount));
        assertEquals(ErrorCode.ILLEGAL_ARGUMENT.getMessage(), exception.getMessage());
    }

    @Test
    public void 잔액충전_동시성() throws InterruptedException {
        // given
        Long userId = 1L;
        Integer amount = 500;
        Balance balance = userRepository.findBalanceByUserId(userId).orElse(null);

        // when
        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> {
                    userBalanceFacade.rechargeBalance(userId, amount);
                }),
                CompletableFuture.runAsync(() -> {
                    userBalanceFacade.rechargeBalance(userId, amount);
                }),
                CompletableFuture.runAsync(() -> {
                    userBalanceFacade.rechargeBalance(userId, amount);
                })
        ).join();

        Thread.sleep(1000);

        // then
        Balance result = userRepository.findBalanceByUserId(userId).orElse(null);

        assertNotNull(balance);
        assertNotNull(result);
        assertEquals(balance.getBalance() + amount, result.getBalance());
    }
}
