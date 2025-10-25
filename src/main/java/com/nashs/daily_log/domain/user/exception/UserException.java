package com.nashs.daily_log.domain.user.exception;

import com.nashs.daily_log.domain.common.exception.DomainException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class UserException extends DomainException {

    @Getter
    @RequiredArgsConstructor
    public enum UserExceptionCode {
        NO_SUCH_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저 입니다.")
        ;

        private final HttpStatus status;
        private final String msg;
    }

    public UserException(UserExceptionCode code) {
        super(code.getStatus(), code.getMsg());
    }
}
