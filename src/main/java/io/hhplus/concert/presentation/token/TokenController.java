package io.hhplus.concert.presentation.token;

import io.hhplus.concert.presentation.token.dto.TokenJoinDto;
import io.hhplus.concert.presentation.token.dto.TokenStatusDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/token")
public class TokenController {

    @PostMapping("/join")
    public ResponseEntity<TokenJoinDto.Response> joinQueue(@RequestBody TokenJoinDto.Request queueRequest) {
        String token = UUID.randomUUID().toString();
        int status = 0; // Enum 값이지만, 여기서는 단순히 0으로 설정
        int position = 100; // Mock 대기번호

        TokenJoinDto.Response response = new TokenJoinDto.Response(token, status, position);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/status")
    public ResponseEntity<TokenStatusDto.Response> getQueueStatus(@RequestHeader("Authorization") String authorization) {
        String token = UUID.randomUUID().toString();
        int status = 0; // Enum 값이지만, 여기서는 단순히 0으로 설정
        int position = 100; // Mock 대기번호

        TokenStatusDto.Response response = new TokenStatusDto.Response(token, status, position);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
