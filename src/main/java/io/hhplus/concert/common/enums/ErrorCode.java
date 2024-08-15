package io.hhplus.concert.common.enums;

import lombok.Getter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    NO_DATA(HttpStatus.NOT_FOUND, "데이터 조회 결과 없음", LogLevel.ERROR),
    TOKEN_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 토큰", LogLevel.ERROR),
    ILLEGAL_ARGUMENT(HttpStatus.INTERNAL_SERVER_ERROR, "잘못된 파라미터 요청", LogLevel.ERROR),
    BALANCE_NOT_EXIST(HttpStatus.NOT_FOUND, "사용자 잔액 정보 없음", LogLevel.ERROR),
    ILLEGAL_PAYMENT_ARGUMENT(HttpStatus.INTERNAL_SERVER_ERROR, "결제 시도 금액이 보유한 금액을 초과", LogLevel.ERROR),
    RESERVATION_LIMIT_EXCEEDED(HttpStatus.OK, "최대 예매 가능 개수 초과", LogLevel.ERROR),
    RESERVATION_CONFLICT(HttpStatus.OK, "이미 선택한 좌석", LogLevel.WARN),
    RESERVATION_PARTIALLY_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "예약 데이터 등록 일부 실패", LogLevel.ERROR),
    JSON_SERIALIZATION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "객체를 JSON String으로 직렬화 실패", LogLevel.ERROR),
    REDIS_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Redis에서 실패", LogLevel.ERROR),
    NO_SUCH_MESSAGE(HttpStatus.BAD_REQUEST, "일치하는 메시지가 없음", LogLevel.ERROR),
    UNKNOWN(HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 에러 발생", LogLevel.ERROR),
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
