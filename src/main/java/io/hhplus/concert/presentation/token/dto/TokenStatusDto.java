package io.hhplus.concert.presentation.token.dto;

public class TokenStatusDto {
    public record Response(String token, int status, int position) {}

}
