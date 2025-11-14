package com.nashs.daily_log.global.interceptor;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.common.exception.DomainException;
import com.nashs.daily_log.global.annotation.LoginRequired;
import com.nashs.daily_log.global.provider.LifeLogUserProvider;
import com.nashs.daily_log.global.resolver.LifeLogUserArgumentResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Component
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final LifeLogUserProvider lifeLogUserProvider;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginRequired loginRequired = handlerMethod.getMethodAnnotation(LoginRequired.class);

        if (loginRequired == null) {
            loginRequired = handlerMethod.getBeanType().getAnnotation(LoginRequired.class);
        }

        if (loginRequired == null) {
            return true;
        }

        var user = lifeLogUserProvider.getCurrentUserOrNull();
        if (user == null) {
            throw new DomainException(UNAUTHORIZED, "로그인이 필요합니다.");
        }

        return true;
    }
}
