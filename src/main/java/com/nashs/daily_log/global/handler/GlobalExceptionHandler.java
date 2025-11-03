package com.nashs.daily_log.global.handler;

import com.nashs.daily_log.domain.webhook.service.WebhookService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler {

    private final WebhookService notifier;

    public record GlobalErrorResponse(HttpStatus status, String msg) {}

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> noRes(NoResourceFoundException ex, HttpServletRequest req) {
        return ResponseEntity.notFound()
                             .build();
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<GlobalErrorResponse> handleRSE(ResponseStatusException ex, HttpServletRequest req) {
        HttpStatus status = ex.getStatusCode() == HttpStatus.UNAUTHORIZED
                          ? HttpStatus.UNAUTHORIZED
                          : HttpStatus.INTERNAL_SERVER_ERROR;
        notifier.sendExceptionAsync(ex, req);

        return ResponseEntity.status(status)
                             .body(new GlobalErrorResponse(status, ex.getReason()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalErrorResponse> handleAny(Exception ex, HttpServletRequest req) {
        notifier.sendExceptionAsync(ex, req);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new GlobalErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }
}
