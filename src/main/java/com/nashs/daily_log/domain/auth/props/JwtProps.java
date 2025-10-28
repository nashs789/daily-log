package com.nashs.daily_log.domain.auth.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProps(
        String issuer,
        String secret,              // Base64 문자열 권장
        int accessExpMinutes,
        int refreshExpDays
) {}
