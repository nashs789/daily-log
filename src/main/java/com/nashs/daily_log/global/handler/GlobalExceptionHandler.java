package com.nashs.daily_log.global.handler;

import com.nashs.daily_log.domain.webhook.service.WebhookService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> noRes(NoResourceFoundException ex, HttpServletRequest req) {
        return ResponseEntity.notFound()
                             .build();
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Void> handleRSE(ResponseStatusException ex, HttpServletRequest req) {
        notifier.sendExceptionAsync(ex, req);

        return ResponseEntity.notFound()
                             .build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleAny(Exception ex, HttpServletRequest req) {
        notifier.sendExceptionAsync(ex, req);

        return ResponseEntity.notFound()
                             .build();
    }
}
