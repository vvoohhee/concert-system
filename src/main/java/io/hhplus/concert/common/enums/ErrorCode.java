package io.hhplus.concert.common.enums;

import lombok.Getter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    NO_DATA(HttpStatus.NOT_FOUND, "데이터 조회 결과 없음", LogLevel.ERROR),
    TOKEN_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 토큰", LogLevel.ERROR),
    ILLEGAL_ARGUMENT(HttpStatus.INTERNAL_SERVER_ERROR, "잘못된 파라미터 요청", LogLevel.ERROR),
    ILLEGAL_PAYMENT_ARGUMENT(HttpStatus.INTERNAL_SERVER_ERROR, "결제 시도 금액이 보유한 금액을 초과", LogLevel.ERROR),
    RESERVATION_LIMIT_EXCEEDED(HttpStatus.OK, "최대 예매 가능 개수 초과", LogLevel.ERROR),
    RESERVATION_CONFLICT(HttpStatus.OK, "이미 선택한 좌석", LogLevel.WARN),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한 없음", LogLevel.ERROR);

    private final HttpStatus httpStatus;
    private final String message;
    private final LogLevel logLevel;

    ErrorCode(HttpStatus httpStatus, String message, LogLevel logLevel) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.logLevel = logLevel;
    }
}
