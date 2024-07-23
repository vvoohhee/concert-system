package io.hhplus.concert.domain.user;

import io.hhplus.concert.common.exception.CustomException;
import io.hhplus.concert.domain.user.dto.RechargeCommand;
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
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("잔액_조회_테스트_성공")
    void findBalanceTest_성공() {
        // given
        Long userId = 1L;
        Integer original = 1000;
        Balance balance = new Balance(userId, original);

        // when
        when(userRepository.findBalanceByUserId(userId)).thenReturn(Optional.of(balance));
        Balance result = userService.findBalance(userId);

        // then
        assertEquals(original, result.getBalance());
    }

    @Test
    @DisplayName("잔액_조회_테스트_실패_조회_정보없음")
    void findBalanceTest_실패_조회_정보없음() {
        // given
        Long userId = 1L;

        // when
        when(userRepository.findBalanceByUserId(userId)).thenReturn(Optional.empty());

        // then
        assertThrows(CustomException.class, () -> userService.findBalance(userId));
    }

    @Test
    @DisplayName("잔액_충전_테스트_성공")
    void rechargeBalanceTest_성공() {
        // given
        Long userId = 1L;
        Integer original = 1000;
        Balance balance = new Balance(userId, original);
        Integer rechargeAmount = 500;

        RechargeCommand request = new RechargeCommand(userId, rechargeAmount);

        // when
        when(userRepository.findBalanceByUserId(userId)).thenReturn(Optional.of(balance));
        Balance result = userService.rechargeBalance(request);

        // then
        assertEquals(original + rechargeAmount, result.getBalance());
    }

    @Test
    @DisplayName("잔액_충전_테스트_실패_조회_정보없음")
    void rechargeBalanceTest_실패_조회_정보없음() {
        // given
        Long userId = 1L;
        Integer rechargeAmount = 500;

        RechargeCommand request = new RechargeCommand(userId, rechargeAmount);

        // when
        when(userRepository.findBalanceByUserId(userId)).thenReturn(Optional.empty());

        // then
        assertThrows(CustomException.class, () -> userService.rechargeBalance(request));
    }
}
