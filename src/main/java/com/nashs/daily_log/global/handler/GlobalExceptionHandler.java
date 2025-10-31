package com.nashs.daily_log.global.handler;

import com.nashs.daily_log.domain.notify.service.NotifyService;
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

import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler {
    private final NotifyService notifier;

    @ExceptionHandler(NoResourceFoundException.class)
    public org.springframework.http.ResponseEntity<Void> noRes(NoResourceFoundException ex) {
        return org.springframework.http.ResponseEntity.notFound()
                                                      .build();
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleRSE(ResponseStatusException e) {
        if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return ResponseEntity.status(401)
                                 .body(Map.of(
                                         "error", "unauthorized",
                                         "message", "Login required"
                                 ));
        }
        return ResponseEntity.status(e.getStatusCode())
                             .body(Map.of(
                                     "error","error",
                                     "message", e.getReason())
                             );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAny(Exception ex, HttpServletRequest req) {
        notifier.sendExceptionAsync(ex, req);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Hello");
    }
}
