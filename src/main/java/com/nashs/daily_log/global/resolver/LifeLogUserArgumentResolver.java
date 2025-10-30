package com.nashs.daily_log.global.resolver;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class LifeLogUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return LifeLogUser.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) {
        var ctx = SecurityContextHolder.getContext();
        var auth = (ctx != null) ? ctx.getAuthentication() : null;
        if (Objects.isNull(auth) || !auth.isAuthenticated()) {
            return null;
        }

        Object p = auth.getPrincipal();

        if (p instanceof OidcUser oidc) {
            var attr = oidc.getAttributes();

            return LifeLogUser.builder()
                              .sub((String) attr.getOrDefault("sub", ""))
                              .email((String) attr.getOrDefault("email", ""))
                              .name((String) attr.getOrDefault("name", attr.getOrDefault("email","")))
                              .picture((String) attr.getOrDefault("picture", null))
                              .roles(List.of("ROLE_USER"))
                              .build();
        }

        return null;
    }
}
