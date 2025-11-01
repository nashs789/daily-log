package com.nashs.daily_log.domain.template.exception;

import com.nashs.daily_log.domain.common.exception.DomainException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class TemplateDomainException extends DomainException {

    @Getter
    @RequiredArgsConstructor
    public enum TemplateDomainExceptionCode {
        NOT_TEMPLATE_OWNER(HttpStatus.NOT_FOUND, "템플릿 소유 유저가 아닙니다.")
        ;

        private final HttpStatus status;
        private final String msg;
    }

    public TemplateDomainException(TemplateDomainExceptionCode code) {
        super(code.getStatus(), code.getMsg());
    }
}
