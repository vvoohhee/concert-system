package io.hhplus.concert.domain.balance;


import io.hhplus.concert.application.balance.UserBalanceFacade;
import io.hhplus.concert.application.balance.UserBalanceService;
import io.hhplus.concert.common.enums.ErrorCode;
import io.hhplus.concert.common.exception.CustomException;
import io.hhplus.concert.domain.balance.dto.BalanceInfo;
import io.hhplus.concert.domain.balance.dto.FindBalanceResponse;
import io.hhplus.concert.domain.balance.dto.RechargeCommand;
import io.hhplus.concert.domain.balance.dto.RechargeResponse;
import io.hhplus.concert.interfaces.presentation.balance.dto.FindBalanceDto;
import io.hhplus.concert.interfaces.presentation.balance.dto.RechargeBalanceDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BalanceIntegrationTest {

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private UserBalanceFacade userBalanceFacade;

    @Test
    public void 잔액조회_성공() {
        // given
        Long userId = 1L;

        // when
        Balance balance = balanceRepository.findByUserId(userId).orElse(null);
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

        Balance balance = balanceRepository.findByUserId(userId).orElse(null);
        BalanceInfo response = userBalanceFacade.recharge(userId, amount);

        assert balance != null;
        assertEquals(balance.getBalance() + amount, response.balance());
    }

    @Test
    public void 잔액충전_실패_유저아이디_NULL() {
        Long userId = null;
        Integer amount = 500;

        CustomException exception = assertThrows(CustomException.class, () -> userBalanceFacade.recharge(userId, amount));
        assertEquals(ErrorCode.NO_DATA.getMessage(), exception.getMessage());
    }

    @Test
    public void 잔액충전_실패_충전금액_NULL() {
        Long userId = 1L;
        Integer amount = null;

        CustomException exception = assertThrows(CustomException.class, () -> userBalanceFacade.recharge(userId, amount));
        assertEquals(ErrorCode.ILLEGAL_ARGUMENT.getMessage(), exception.getMessage());
    }

    @Test
    public void 잔액충전_실패_충전금액_음수() {
        Long userId = 1L;
        Integer amount = -500;

        CustomException exception = assertThrows(CustomException.class, () -> userBalanceFacade.recharge(userId, amount));
        assertEquals(ErrorCode.ILLEGAL_ARGUMENT.getMessage(), exception.getMessage());
    }

    @Test
    public void 잔액충전_동시성() throws InterruptedException {
        // given
        Long userId = 1L;
        Integer amount = 500;
        Balance balance = balanceRepository.findByUserId(userId).orElse(null);

        // when
        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> {
                    userBalanceFacade.recharge(userId, amount);
                }),
                CompletableFuture.runAsync(() -> {
                    userBalanceFacade.recharge(userId, amount);
                }),
                CompletableFuture.runAsync(() -> {
                    userBalanceFacade.recharge(userId, amount);
                })
        ).join();

        Thread.sleep(1000);

        // then
        Balance result = balanceRepository.findByUserId(userId).orElse(null);

        assertNotNull(balance);
        assertNotNull(result);
        assertEquals(balance.getBalance() + amount, result.getBalance());
    }
}
