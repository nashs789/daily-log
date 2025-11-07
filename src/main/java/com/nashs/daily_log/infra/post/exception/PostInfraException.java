package com.nashs.daily_log.infra.post.exception;

import com.nashs.daily_log.infra.common.exception.InfraException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

public class PostInfraException extends InfraException {

    @Getter
    @RequiredArgsConstructor
    public enum PostInfraExceptionCode {
        UPDATE_NOTHING(INTERNAL_SERVER_ERROR, "수정 중 에러가 발생했습니다.")
        ;

        private final HttpStatus status;
        private final String msg;
    }

    public PostInfraException(PostInfraExceptionCode code) {
        super(code.getStatus(), code.getMsg());
    }
}
