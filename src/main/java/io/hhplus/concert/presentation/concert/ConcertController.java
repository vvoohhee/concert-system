package io.hhplus.concert.presentation.concert;


import io.hhplus.concert.application.concert.UserConcertService;
import io.hhplus.concert.presentation.concert.dto.ConcertReserveDto;
import io.hhplus.concert.presentation.concert.dto.ConcertsDto;
import io.hhplus.concert.presentation.concert.dto.GetConcertSeatListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/concert")
public class ConcertController {

    private final UserConcertService userConcertService;

    @GetMapping("/queue")
    public ResponseEntity<ConcertsDto.Response> concerts(
            @RequestParam("at") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime reserveAt
    ) {
        return ResponseEntity.ok(
                ConcertsDto.Response.of(userConcertService.findConcerts(reserveAt))
        );
    }

    @GetMapping("/queue/{concertOptionId}/seats")
    public ResponseEntity<GetConcertSeatListDto.Response> seats(@PathVariable Long concertOptionId) {
        return ResponseEntity.ok(
                GetConcertSeatListDto.Response.of(userConcertService.findSeats(concertOptionId))
        );
    }

    @PostMapping("/queue/reserve")
    public ResponseEntity<ConcertReserveDto.Response> reserveSeats(
            @RequestBody ConcertReserveDto.Request request,
            @RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(
                ConcertReserveDto.Response.of(userConcertService.reserveSeats(request.seatIdList(), authorization))
        );
    }
}