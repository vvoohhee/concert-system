package io.hhplus.concert.interfaces.presentation.concert;


import io.hhplus.concert.application.concert.UserConcertService;
import io.hhplus.concert.interfaces.presentation.concert.dto.ConcertReserveDto;
import io.hhplus.concert.interfaces.presentation.concert.dto.ConcertsDto;
import io.hhplus.concert.interfaces.presentation.concert.dto.GetConcertSeatListDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/concert")
@Tag(name = "Concert", description = "콘서트 관련 API")
public class ConcertController {

    private final UserConcertService userConcertService;

    @GetMapping("/queue")
    @Operation(summary = "콘서트 리스트 조회", description = "요청한 날짜에 예약 가능한 콘서트 리스트 조회")
    public ResponseEntity<ConcertsDto.Response> concerts(
            @RequestParam("at") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime reserveAt
    ) {
        return ResponseEntity.ok(
                ConcertsDto.Response.of(userConcertService.findConcerts(reserveAt))
        );
    }

    @GetMapping("/queue/{concertOptionId}/seats")
    @Operation(summary = "콘서트 좌석 조회", description = "선택한 콘서트의 좌석 정보를 조회")
    public ResponseEntity<GetConcertSeatListDto.Response> seats(@PathVariable Long concertOptionId) {
        return ResponseEntity.ok(
                GetConcertSeatListDto.Response.of(userConcertService.findSeats(concertOptionId))
        );
    }

    @PostMapping("/queue/reserve")
    @Operation(summary = "콘서트 예약", description = "콘서트 좌석을 1개 이상 선택하여 예약")
    public ResponseEntity<ConcertReserveDto.Response> reserveSeats(
            @RequestBody ConcertReserveDto.Request request,
            @RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(
                ConcertReserveDto.Response.of(userConcertService.reserveSeats(request.seatIdList(), authorization))
        );
    }
}