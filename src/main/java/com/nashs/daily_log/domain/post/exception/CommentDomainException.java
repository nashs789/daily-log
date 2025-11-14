package com.nashs.daily_log.domain.post.exception;

import com.nashs.daily_log.domain.common.exception.DomainException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class CommentDomainException extends DomainException {

    @Getter
    @RequiredArgsConstructor
    public enum CommentDomainExceptionCode {
        NOT_COMMENT_OWNER(BAD_REQUEST, "댓글 소유주가 아닙니다.")
        ;

        private final HttpStatus status;
        private final String msg;
    }

    public CommentDomainException(CommentDomainExceptionCode code) {
        super(code.getStatus(), code.getMsg());
    }
}
