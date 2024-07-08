package io.hhplus.concert.domain.balance;

import io.hhplus.concert.common.enums.BalanceHistoryType;
import io.hhplus.concert.domain.balance.command.SaveBalanceHistoryRequestCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BalanceHistoryServiceTest {
    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private BalanceHistoryRepository balanceHistoryRepository;

    @InjectMocks
    private BalanceHistoryService balanceHistoryService;

    @Test
    @DisplayName("잔액_히스토리_생성_테스트_성공")
    void saveTest_성공() {
        // given
        Integer amount = 500;
        Balance balance = new Balance(1L, 1L, 1000, LocalDateTime.now(), LocalDateTime.now());

        BalanceHistory balanceHistory = new BalanceHistory(balance.getId(), amount, BalanceHistoryType.RECHARGE);
        balanceHistory.setId(1L);
        balanceHistory.setCreatedAt(LocalDateTime.now());

        SaveBalanceHistoryRequestCommand command = new SaveBalanceHistoryRequestCommand(balance.getId(), amount, BalanceHistoryType.RECHARGE);

        // when
        when(balanceHistoryRepository.save(balanceHistory)).thenReturn(balanceHistory);

        // then
        assertEquals(balanceHistory, balanceHistoryService.save(command));
    }
}
