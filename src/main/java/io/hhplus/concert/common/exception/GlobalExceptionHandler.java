package io.hhplus.concert.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleException(CustomException e) {
        // 에러 발생 로그 남기기
        switch (e.getErrorCode().getLogLevel()) {
            case ERROR -> log.error("[CustomException][ERROR] : {}", e.getMessage(), e);
            case WARN -> log.warn("[CustomException][WARN] : {}", e.getMessage(), e);
            default -> log.info("[CustomException][INFO] : {}", e.getMessage(), e);
        }

        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode().getHttpStatus(), e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(errorResponse);
    }
}
