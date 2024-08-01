package io.hhplus.concert.interfaces.presentation.token.dto;

import io.hhplus.concert.common.enums.TokenStatusType;
import io.hhplus.concert.domain.token.TokenInfo;


public class TokenStatusDto {
    public record Response(TokenStatusType status, Long position) {
        public static Response of(TokenInfo token) {
            return new Response(token.status(), token.position());
        }
    }

}
