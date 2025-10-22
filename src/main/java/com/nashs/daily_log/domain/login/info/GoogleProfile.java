package com.nashs.daily_log.domain.login.info;

import java.util.Map;

public record GoogleProfile(String sub, String email, String name, String picture) {
    public static GoogleProfile fromAttributes(Map<String, Object> a) {
        return new GoogleProfile(
                (String) a.get("sub"),
                (String) a.get("email"),
                (String) a.getOrDefault("name", ""),
                (String) a.getOrDefault("picture", "")
        );
    }
}
