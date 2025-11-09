package com.nashs.daily_log.global.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ValidationExceptionHandler {

    public record FieldError(String field, String msg) {}
    public record ValidErrorResponse(String code, String msg, List<FieldError> fieldErrors) {}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidErrorResponse> handle(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ValidErrorResponse("VALIDATION ERROR",
                                                          "입력값을 확인하세요.",
                                                          ex.getBindingResult()
                                                            .getFieldErrors()
                                                            .stream()
                                                            .map(fe -> new FieldError(fe.getField(), fe.getDefaultMessage()))
                                                            .toList())
        );
    }
}
