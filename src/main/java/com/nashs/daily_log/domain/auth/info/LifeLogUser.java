package com.nashs.daily_log.domain.auth.info;

import lombok.Builder;

import java.util.List;

@Builder
public record LifeLogUser(
        Long id,
        String sub,
        String email,
        String name,
        String picture,
        List<String> roles
) {
}
