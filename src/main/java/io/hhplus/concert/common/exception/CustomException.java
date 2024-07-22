package io.hhplus.concert.common.exception;

import io.hhplus.concert.common.enums.ErrorCode;
import lombok.Getter;
import org.springframework.boot.logging.LogLevel;

@Getter
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
