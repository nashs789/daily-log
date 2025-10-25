package com.nashs.daily_log.domain.user.repository;

import com.nashs.daily_log.domain.user.info.UserInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    boolean isRegisteredUser(String sub);
    UserInfo saveSocialUser(UserInfo userInfo);
    UserInfo findBySub(String sub);
}
