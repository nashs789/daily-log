package com.nashs.daily_log.infra.login.repository.impl;

import com.nashs.daily_log.domain.login.info.UserInfo;
import com.nashs.daily_log.domain.login.repository.UserRepository;
import com.nashs.daily_log.infra.login.entity.User;
import com.nashs.daily_log.infra.login.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public boolean isRegisteredUser(String sub) {
        return userJpaRepository.findBySub(sub)
                                .isPresent();
    }

    @Override
    public UserInfo saveSocialUser(UserInfo userInfo) {
        return userJpaRepository.save(User.builder()
                                          .id(userInfo.getId())
                                          .sub(userInfo.getSub())
                                          .email(userInfo.getEmail())
                                          .provider(userInfo.getProvider())
                                          .emailVerified(userInfo.isEmailVerified())
                                          .username(userInfo.getUsername())
                                          .picture(userInfo.getPicture())
                                          .build())
                                .toUserInfo();
    }
}
