package com.nashs.daily_log.domain.user.info;

import com.nashs.daily_log.infra.login.entity.User.Provider;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo {
    private Long id;
    private String sub;
    private String email;
    private Provider provider;
    private boolean emailVerified;
    private String username;
    private String picture;
}
