package com.nashs.daily_log.global.resolver;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.global.provider.LifeLogUserProvider;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class LifeLogUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final LifeLogUserProvider lifeLogUserProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return LifeLogUser.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) {
        return lifeLogUserProvider.getCurrentUserOrNull();
    }
}
