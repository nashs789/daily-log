package com.nashs.daily_log.domain.auth.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.cookie")
public record AuthCookieProps(
        String domain,
        boolean secure,
        String sameSite,
        String accessName,
        String refreshName,
        int accessMaxAgeMins,
        int refreshMaxAgeDays
) {
}
