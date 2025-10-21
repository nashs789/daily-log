package com.nashs.daily_log.domain.login.info;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nashs.daily_log.infra.login.entity.User;

public record GoogleAttrs(
        String sub,
        String email,
        @JsonProperty("email_verified") Boolean isEmailVerified,
        String name,
        String picture
) {
    public UserInfo toUserInfo() {
        return UserInfo.builder()
                       .sub(sub)
                       .email(email)
                       .emailVerified(isEmailVerified)
                       .provider(User.Provider.GOOGLE)
                       .username(name)
                       .picture(picture)
                       .build();
    }
}
