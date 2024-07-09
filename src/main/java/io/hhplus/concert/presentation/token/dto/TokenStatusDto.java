package io.hhplus.concert.presentation.token.dto;

import io.hhplus.concert.common.enums.TokenStatusType;

import java.time.LocalDateTime;

public class TokenStatusDto {
    public record Response(String token, TokenStatusType status, int position, LocalDateTime expiresAt) {}

}
