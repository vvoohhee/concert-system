package io.hhplus.concert.presentation.payment;

import io.hhplus.concert.presentation.concert.dto.ConcertPaymentDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
    @PostMapping("/seat")
    public ResponseEntity<ConcertPaymentDto.Response> processPayment(
            @RequestHeader("Authorization") String authorization) {
        // Mock 데이터 생성
        List<ConcertPaymentDto.Ticket> ticketList = new ArrayList<>();
        ticketList.add(new ConcertPaymentDto.Ticket(
                1L,
                new ConcertPaymentDto.ConcertInfo(1L, 50000, LocalDateTime.now(), LocalDateTime.now()),
                new ConcertPaymentDto.SeatInfo(101L, 1)
        ));

        // 결제 결과 생성
        int status = 0; // 성공 상태 코드
        String description = "Payment successful"; // 상태 설명

        ConcertPaymentDto.Response response = new ConcertPaymentDto.Response(status, description, ticketList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
