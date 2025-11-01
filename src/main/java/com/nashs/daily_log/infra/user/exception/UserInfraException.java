package com.nashs.daily_log.infra.user.exception;

import com.nashs.daily_log.infra.common.exception.InfraException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public class UserInfraException extends InfraException {

    @Getter
    @RequiredArgsConstructor
    public enum UserInfraExceptionCode {
        NO_SUCH_USER(HttpStatus.NOT_FOUND, "존재하지 않는 유저 입니다.")
        ;

        private final HttpStatus status;
        private final String msg;
    }

    public UserInfraException(UserInfraExceptionCode code) {
        super(code.getStatus(), code.getMsg());
    }
}
