package io.hhplus.concert.presentation.token.dto;

public class TokenJoinDto {
    public record Request(Long userId) {
    }

    public record Response(String token, int status, int position) {
    }
}