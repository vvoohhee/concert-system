package io.hhplus.concert.presentation.token.dto;

import io.hhplus.concert.common.enums.TokenStatusType;
import io.hhplus.concert.domain.token.TokenInfo;

public class IssueTokenDto {
    public record Request(Long userId) {
    }

    public record Response(String token, TokenStatusType status) {
        public static Response of(TokenInfo token) {
            return new Response(token.token(), token.status());
        }
    }
}