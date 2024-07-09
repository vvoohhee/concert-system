package io.hhplus.concert.presentation.concert;


import io.hhplus.concert.application.concert.ConcertService;
import io.hhplus.concert.presentation.concert.dto.ConcertOptionListDto;
import io.hhplus.concert.presentation.concert.dto.ConcertPaymentDto;
import io.hhplus.concert.presentation.concert.dto.ConcertReserveDto;
import io.hhplus.concert.presentation.concert.dto.GetConcertSeatListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/concert")
public class ConcertController {

    private final ConcertService concertService;

    @GetMapping("/{concertId}")
    public ResponseEntity<ConcertOptionListDto.Response> concertOptions(
            @PathVariable Long concertId,
            @RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(concertService.findConcertOptions(concertId, authorization));
    }


    @GetMapping("/{concertId}")
    public ResponseEntity<GetConcertSeatListDto.Response> seats(
            @PathVariable Long concertId,
            @RequestParam("at") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startAt,
            @RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(concertService.findSeats(concertId, startAt, authorization));
    }

    @PostMapping("/reserve")
    public ResponseEntity<ConcertReserveDto.Response> reserveSeats(
            @RequestBody ConcertReserveDto.Request request,
            @RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(concertService.reserveSeats(request, authorization));
    }
}