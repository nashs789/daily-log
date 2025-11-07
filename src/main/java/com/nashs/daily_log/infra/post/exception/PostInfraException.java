package com.nashs.daily_log.infra.post.exception;

import com.nashs.daily_log.infra.common.exception.InfraException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class PostInfraException extends InfraException {

    @Getter
    @RequiredArgsConstructor
    public enum PostInfraExceptionCode {
        NO_SUCH_POST(NOT_FOUND, "존재하지 않는 게시글 입니다."),
        ;

        private final HttpStatus status;
        private final String msg;
    }

    public PostInfraException(PostInfraExceptionCode code) {
        super(code.getStatus(), code.getMsg());
    }
}
