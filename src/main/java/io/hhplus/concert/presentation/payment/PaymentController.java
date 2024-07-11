package io.hhplus.concert.presentation.payment;

import io.hhplus.concert.application.payment.UserPaymentService;
import io.hhplus.concert.presentation.concert.dto.ConcertPaymentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Tag(name = "Payment", description = "콘서트 결제 관련 API")
public class PaymentController {

    private final UserPaymentService userPaymentService;

    @PostMapping("/seats")
    @Operation(summary = "콘서트 티켓 결제", description = "예약한 콘서트 티켓을 결제")
    public ResponseEntity<ConcertPaymentDto.Response> billing(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(
                ConcertPaymentDto.Response.of(userPaymentService.billing(authorization))
        );
    }
}
