package io.hhplus.concert.presentation.concert;


import io.hhplus.concert.presentation.concert.dto.ConcertOptionListDto;
import io.hhplus.concert.presentation.concert.dto.ConcertPaymentDto;
import io.hhplus.concert.presentation.concert.dto.ConcertReserveDto;
import io.hhplus.concert.presentation.concert.dto.GetConcertSeatListDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/concert")
public class ConcertController {

    @GetMapping("/{concertId}/option")
    public ResponseEntity<ConcertOptionListDto.Response> getConcertOptions(
            @PathVariable Long concertId,
            @RequestHeader("Authorization") String authorization) {
        // Mock 데이터 생성
        ConcertOptionListDto.ConcertOption option1 = new ConcertOptionListDto.ConcertOption(
                1L,
                "Concert A - Evening Show",
                200,
                50000L,
                4,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        List<ConcertOptionListDto.ConcertOption> concertOptionList = List.of(option1);
        ConcertOptionListDto.Response response = new ConcertOptionListDto.Response(concertOptionList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/{concertId}/option/{optionId}/seat")
    public ResponseEntity<GetConcertSeatListDto.Response> getConcertOptionSeats(
            @PathVariable Long concertId,
            @PathVariable Long optionId,
            @RequestHeader("Authorization") String authorization) {
        // Mock 데이터 생성
        GetConcertSeatListDto.Seat seat1 = new GetConcertSeatListDto.Seat(1L, 101, 0); // 0: 예약 가능
        GetConcertSeatListDto.Seat seat2 = new GetConcertSeatListDto.Seat(2L, 102, 1); // 1: 예약 대기

        List<GetConcertSeatListDto.Seat> seatList = List.of(seat1, seat2);
        GetConcertSeatListDto.Response response = new GetConcertSeatListDto.Response(optionId, seatList);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/reserve")
    public ResponseEntity<ConcertReserveDto.Response> reserveSeats(
            @RequestBody ConcertReserveDto.Request request,
            @RequestHeader("Authorization") String authorization) {
        int status = 0; // 성공 상태 코드
        String description = "좌석 예약 성공"; // 상태 설명

        ConcertReserveDto.Response response = new ConcertReserveDto.Response(status, description);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/payment")
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