package com.nashs.daily_log.domain.post.exception;

import com.nashs.daily_log.domain.common.exception.DomainException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class PostDomainException extends DomainException {

    @Getter
    @RequiredArgsConstructor
    public enum PostDomainExceptionCode {
        NOT_POST_OWNER(HttpStatus.BAD_REQUEST, "게시글 소유주가 아닙니다.")
        ;

        private final HttpStatus status;
        private final String msg;
    }

    public PostDomainException(PostDomainExceptionCode code) {
        super(code.getStatus(), code.getMsg());
    }
}
