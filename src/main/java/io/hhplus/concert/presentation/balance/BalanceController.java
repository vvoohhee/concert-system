package io.hhplus.concert.presentation.balance;


import io.hhplus.concert.presentation.balance.dto.GetBalanceDto;
import io.hhplus.concert.presentation.balance.dto.RechargeBalanceDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/balance")
public class BalanceController {

    @GetMapping("/{userId}")
    public ResponseEntity<GetBalanceDto.Response> getBalance(@PathVariable Long userId) {
        int balance = 50000; // 임의의 잔액을 설정

        GetBalanceDto.Response response = new GetBalanceDto.Response(balance);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<RechargeBalanceDto.Response> rechargeBalance(
            @RequestBody RechargeBalanceDto.Request request) {
        int currentBalance = 50000;
        int amount = 10000;
        int updatedBalance = currentBalance + amount;

        int status = 0; // 상태 코드 (성공 코드)
        String description = "충전 성공"; // 상태 코드 설명

        RechargeBalanceDto.Response response = new RechargeBalanceDto.Response(status, description, updatedBalance);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}