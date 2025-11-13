package com.nashs.daily_log.domain.user.info;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.infra.user.entity.User.Provider;
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

    public static UserInfo ref(String sub) {
        return UserInfo.builder()
                       .sub(sub)
                       .build();
    }

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
