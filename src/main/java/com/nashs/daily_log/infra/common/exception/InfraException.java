package com.nashs.daily_log.infra.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InfraException extends RuntimeException {

    private final HttpStatus status;

    public InfraException(final HttpStatus status, final String msg) {
        super(msg);
        this.status = status;
    }
}
