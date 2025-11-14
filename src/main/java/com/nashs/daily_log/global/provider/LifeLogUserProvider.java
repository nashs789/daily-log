package com.nashs.daily_log.global.provider;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class LifeLogUserProvider {

    public LifeLogUser getCurrentUserOrNull() {
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
                              .name((String) attr.getOrDefault("name", attr.getOrDefault("email", "")))
                              .picture((String) attr.getOrDefault("picture", null))
                              .roles(List.of("ROLE_USER"))
                              .build();
        }

        return null;
    }
}
