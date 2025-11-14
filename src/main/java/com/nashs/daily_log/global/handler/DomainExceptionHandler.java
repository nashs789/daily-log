package com.nashs.daily_log.global.handler;

import com.nashs.daily_log.domain.common.exception.DomainException;
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

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DomainExceptionHandler {

    private final WebhookService notifier;

    public record DomainErrorResponse(HttpStatus status, String msg) {}

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<DomainErrorResponse> domainExceptionHandler(DomainException ex, HttpServletRequest req) {
        notifier.sendExceptionAsync(ex, req);

        return ResponseEntity.status(ex.getStatus())
                             .body(new DomainErrorResponse(ex.getStatus(), ex.getMessage()));
    }
}
