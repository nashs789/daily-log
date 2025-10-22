package com.nashs.daily_log.domain.login.repository;

import com.nashs.daily_log.domain.login.info.UserInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    boolean isRegisteredUser(String sub);
    UserInfo saveSocialUser(UserInfo userInfo);
}
