package com.nashs.daily_log.infra.template.exception;

import com.nashs.daily_log.infra.common.exception.InfraException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class TemplateInfraException extends InfraException {

    @Getter
    @RequiredArgsConstructor
    public enum TemplateInfraExceptionCode {
        NO_SUCH_TEMPLATE(HttpStatus.NOT_FOUND, "존재하지 않는 템플릿 입니다.")
        ;

        private final HttpStatus status;
        private final String msg;
    }

    public TemplateInfraException(TemplateInfraExceptionCode code) {
        super(code.getStatus(), code.getMsg());
    }
}
