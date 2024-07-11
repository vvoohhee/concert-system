package io.hhplus.concert.presentation.token;

import io.hhplus.concert.application.token.UserTokenService;
import io.hhplus.concert.presentation.token.dto.IssueTokenDto;
import io.hhplus.concert.presentation.token.dto.TokenStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/token")
public class TokenController {

    private final UserTokenService userTokenService;

    @PostMapping("/issue")
    public ResponseEntity<IssueTokenDto.Response> issue(@RequestBody IssueTokenDto.Request request) {
        return ResponseEntity.ok(
                IssueTokenDto.Response.of(userTokenService.issueUserToken(request.userId()))
        );
    }

    @GetMapping("/status")
    public ResponseEntity<TokenStatusDto.Response> find(@RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(
                TokenStatusDto.Response.of(userTokenService.findUserToken(authorization))
        );
    }
}
