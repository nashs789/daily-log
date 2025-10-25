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

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {
    private final NotifyService notifier;

    // TODO - Client 응답 Object 에서 나중에 Exception 정의하고 바꾸기
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAny(Exception ex, HttpServletRequest req) {
        notifier.sendExceptionAsync(ex, req);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Hello");
    }
}
