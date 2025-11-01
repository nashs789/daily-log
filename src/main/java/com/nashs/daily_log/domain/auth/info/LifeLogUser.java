package com.nashs.daily_log.domain.auth.info;

import com.nashs.daily_log.domain.user.info.UserInfo;
import lombok.Builder;

import java.util.List;

@Builder
public record LifeLogUser(
        String sub,
        String email,
        String name,
        String picture,
        List<String> roles
) {
    public UserInfo toUserInfo() {
        return UserInfo.builder()
                       .sub(sub)
                       .email(email)
                       .username(name)
                       .picture(picture)
                       .build();
    }
}
