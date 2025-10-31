package com.nashs.daily_log.domain.user.info;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.infra.auth.entity.User.Provider;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserInfo {
    private String sub;
    private String email;
    private Provider provider;
    private boolean emailVerified;
    private String username;
    private String picture;

    public LifeLogUser toLifeLogUser() {
        return LifeLogUser.builder()
                          .sub(sub)
                          .email(email)
                          .name(username)
                          .picture(picture)
                          .roles(List.of("ROLE_USER"))
                          .build();
    }
}
