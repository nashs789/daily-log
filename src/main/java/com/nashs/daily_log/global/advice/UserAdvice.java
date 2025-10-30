package com.nashs.daily_log.global.advice;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import jakarta.annotation.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class UserAdvice {

    @ModelAttribute("lifeLogUser")
    public LifeLogUser me(@Nullable Authentication auth) {
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
