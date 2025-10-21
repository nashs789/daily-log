package com.nashs.daily_log.domain.login.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashs.daily_log.domain.login.info.GoogleAttrs;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoogleAttributeParser {

    private final ObjectMapper objectMapper;

    public GoogleAttrs fromPrincipal(OAuth2User principal) {
        return objectMapper.convertValue(principal.getAttributes(), GoogleAttrs.class);
    }
}
