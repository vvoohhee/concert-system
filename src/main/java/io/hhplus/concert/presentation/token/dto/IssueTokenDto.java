package io.hhplus.concert.presentation.token.dto;

import io.hhplus.concert.common.enums.TokenStatusType;

import java.time.LocalDateTime;

public class IssueTokenDto {
    public record Request(Long userId) {
    }

    public record Response(String token, TokenStatusType status, LocalDateTime expiresAt) {
    }
}