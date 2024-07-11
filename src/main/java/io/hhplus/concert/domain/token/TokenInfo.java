package io.hhplus.concert.domain.token;

import io.hhplus.concert.common.enums.TokenStatusType;

public record TokenInfo(
        String token,
        TokenStatusType status,
        Integer position
) {
}
