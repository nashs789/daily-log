package com.nashs.daily_log.infra.login.repository.impl;

import com.nashs.daily_log.domain.user.exception.UserException;
import com.nashs.daily_log.domain.user.info.UserInfo;
import com.nashs.daily_log.domain.user.repository.UserRepository;
import com.nashs.daily_log.infra.login.entity.User;
import com.nashs.daily_log.infra.login.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.nashs.daily_log.domain.user.exception.UserException.UserExceptionCode.NO_SUCH_USER;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public boolean isRegisteredUser(final String sub) {
        return userJpaRepository.findBySub(sub)
                                .isPresent();
    }

    @Override
    public UserInfo saveSocialUser(final UserInfo userInfo) {
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

    @Override
    public UserInfo findBySub(final String sub) {
        return userJpaRepository.findBySub(sub)
                                .orElseThrow(() -> new UserException(NO_SUCH_USER))
                                .toUserInfo();
    }
}
