package com.reconplatform.review_service.exception;

import com.reconplatform.common.error.AppException;
import com.reconplatform.common.error.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<Map<String, String>> handleApp(AppException e) {
        HttpStatus status = switch (e.getErrorCode()) {
            case REVIEW_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case REVIEW_ALREADY_RESOLVED -> HttpStatus.CONFLICT;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
        return ResponseEntity.status(status)
                .body(Map.of("code", e.getErrorCode().getCode(), "message", e.getMessage()));
    }
}