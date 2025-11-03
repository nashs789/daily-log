package com.nashs.daily_log.domain.webhook.exception;

import com.nashs.daily_log.domain.common.exception.DomainException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class WebhookException extends DomainException {

    @Getter
    @RequiredArgsConstructor
    public enum WebhookExceptionCode {
        URL_NOT_VALID(HttpStatus.BAD_REQUEST, "유효한 Webhook URL 이 아닙니다.")
        ;

        private final HttpStatus status;
        private final String msg;
    }

    public WebhookException(WebhookExceptionCode code) {
        super(code.getStatus(), code.getMsg());
    }
}
