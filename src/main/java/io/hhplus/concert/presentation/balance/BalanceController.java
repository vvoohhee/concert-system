package io.hhplus.concert.presentation.balance;

import io.hhplus.concert.application.balance.UserBalanceService;
import io.hhplus.concert.presentation.balance.dto.FindBalanceDto;
import io.hhplus.concert.presentation.balance.dto.RechargeBalanceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/balance")
public class BalanceController {

    private final UserBalanceService userBalanceService;

    @GetMapping("/{userId}")
    public ResponseEntity<FindBalanceDto.Response> find(@PathVariable Long userId) {
        return ResponseEntity.ok(userBalanceService.findUserBalance(userId));
    }

    @PutMapping("")
    public ResponseEntity<RechargeBalanceDto.Response> recharge(
            @RequestBody RechargeBalanceDto.Request request) {
        return ResponseEntity.ok(userBalanceService.recharge(request.userId(), request.amount()));
    }
}