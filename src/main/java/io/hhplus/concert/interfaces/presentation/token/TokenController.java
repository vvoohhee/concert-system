package io.hhplus.concert.interfaces.presentation.token;

import io.hhplus.concert.application.token.UserTokenService;
import io.hhplus.concert.interfaces.presentation.token.dto.IssueTokenDto;
import io.hhplus.concert.interfaces.presentation.token.dto.TokenStatusDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
@Tag(name = "Token", description = "대기열 토큰 관련 API")
public class TokenController {

    private final UserTokenService userTokenService;

    @PostMapping("/issue")
    @Operation(summary = "토큰 발급", description = "대기열 토큰 발급")
    public ResponseEntity<IssueTokenDto.Response> issue(@RequestBody IssueTokenDto.Request request) {
        return ResponseEntity.ok(
                IssueTokenDto.Response.of(userTokenService.issueWaitingToken(request.userId()))
        );
    }

    @GetMapping("/status")
    @Operation(summary = "토큰 상태 조회", description = "대기열 토큰의 순서와 상태를 조회")
    public ResponseEntity<TokenStatusDto.Response> find(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(
                TokenStatusDto.Response.of(userTokenService.findWaitingToken(authorization))
        );
    }
}
