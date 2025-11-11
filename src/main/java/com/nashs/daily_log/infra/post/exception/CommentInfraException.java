package com.nashs.daily_log.infra.post.exception;

import com.nashs.daily_log.infra.common.exception.InfraException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class CommentInfraException extends InfraException {

    @Getter
    @RequiredArgsConstructor
    public enum CommentInfraExceptionCode {
        NO_SUCH_COMMENT(NOT_FOUND, "존재하지 않는 댓글 입니다."),
        ;

        private final HttpStatus status;
        private final String msg;
    }

    public CommentInfraException(CommentInfraExceptionCode code) {
        super(code.getStatus(), code.getMsg());
    }
}
