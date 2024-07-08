package io.hhplus.concert.domain.balance;

import io.hhplus.concert.common.exception.NotFoundException;
import io.hhplus.concert.domain.balance.command.FindBalanceResponseCommand;
import io.hhplus.concert.domain.balance.command.RechargeRequestCommand;
import io.hhplus.concert.domain.balance.command.RechargeResponseCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BalanceServiceTest {
    @Mock
    private BalanceRepository balanceRepository;

    @InjectMocks
    private BalanceService balanceService;

    @Test
    @DisplayName("잔액_조회_테스트_성공")
    void findTest_성공() {
        // given
        Long userId = 1L;
        Integer original = 1000;
        Balance balance = new Balance(userId, original);

        // when
        when(balanceRepository.findByUserId(userId)).thenReturn(Optional.of(balance));
        FindBalanceResponseCommand result = balanceService.find(userId);

        // then
        assertEquals(original, result.balance());
    }

    @Test
    @DisplayName("잔액_조회_테스트_실패_조회_정보없음")
    void findTest_실패_조회_정보없음() {
        // given
        Long userId = 1L;

        // when
        when(balanceRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> balanceService.find(userId));
    }

    @Test
    @DisplayName("잔액_충전_테스트_성공")
    void rechargeTest_성공() {
        // given
        Long userId = 1L;
        Integer original = 1000;
        Balance balance = new Balance(userId, original);
        Integer rechargeAmount = 500;

        RechargeRequestCommand request = new RechargeRequestCommand(userId, rechargeAmount);

        // when
        when(balanceRepository.findByUserId(userId)).thenReturn(Optional.of(balance));
        RechargeResponseCommand result = balanceService.recharge(request);

        // then
        assertEquals(original + rechargeAmount, result.balance());
    }

    @Test
    @DisplayName("잔액_충전_테스트_실패_조회_정보없음")
    void rechargeTest_실패_조회_정보없음() {
        // given
        Long userId = 1L;
        Integer rechargeAmount = 500;

        RechargeRequestCommand request = new RechargeRequestCommand(userId, rechargeAmount);

        // when
        when(balanceRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> balanceService.recharge(request));
    }
}
