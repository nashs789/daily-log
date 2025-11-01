package com.nashs.daily_log.domain.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DomainException extends RuntimeException{

    private HttpStatus status;

    public DomainException(final HttpStatus status, final String msg) {
        super(msg);
        this.status = status;
    }
}
