package com.mysite.sbb.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApi(ApiException e, HttpServletRequest req) {
        ApiErrorResponse body = new ApiErrorResponse(
                OffsetDateTime.now().toString(),
                e.getStatus().value(),
                e.getCode(),
                e.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(e.getStatus()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAny(Exception e, HttpServletRequest req) {
        // 콘솔에 원인 출력
        e.printStackTrace();

        ApiErrorResponse body = new ApiErrorResponse(
                java.time.OffsetDateTime.now().toString(),
                500,
                "INTERNAL_500",
                e.getClass().getSimpleName() + ": " + e.getMessage(), //메시지 노출
                req.getRequestURI()
        );
        return ResponseEntity.status(500).body(body);
    }
}