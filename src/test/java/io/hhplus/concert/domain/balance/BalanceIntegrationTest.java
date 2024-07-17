package io.hhplus.concert.domain.balance;


import io.hhplus.concert.application.balance.UserBalanceFacade;
import io.hhplus.concert.domain.balance.command.FindBalanceResponse;
import io.hhplus.concert.domain.balance.command.RechargeRequestCommand;
import io.hhplus.concert.domain.balance.command.RechargeResponse;
import io.hhplus.concert.interfaces.presentation.balance.dto.FindBalanceDto;
import io.hhplus.concert.interfaces.presentation.balance.dto.RechargeBalanceDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BalanceIntegrationTest {

    @Mock
    private BalanceService balanceService;

    @Mock
    private BalanceHistoryService balanceHistoryService;

    @InjectMocks
    private UserBalanceFacade userBalanceFacade;

    @Test
    public void userBalanceTest() {
        Long userId = 1L;
        Integer balanceAmount = 1000;

        FindBalanceResponse responseCommand = new FindBalanceResponse(balanceAmount, LocalDateTime.now(), LocalDateTime.now());
        when(balanceService.find(userId)).thenReturn(responseCommand);

        FindBalanceDto.Response response = userBalanceFacade.findUserBalance(userId);

        assertEquals(balanceAmount, response.balance());
    }

    @Test
    public void rechargeTest() {
        Long userId = 1L;
        Integer amount = 500;
        Long rechargeId = 1L;

        RechargeResponse rechargeResponse = new RechargeResponse(rechargeId, amount);
        when(balanceService.recharge(any(RechargeRequestCommand.class))).thenReturn(rechargeResponse);

        RechargeBalanceDto.Response response = userBalanceFacade.recharge(userId, amount);

        assertEquals(amount, response.balance());
    }
}
