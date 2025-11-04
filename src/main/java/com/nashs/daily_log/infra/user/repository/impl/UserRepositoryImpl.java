package com.nashs.daily_log.infra.user.repository.impl;

import com.nashs.daily_log.infra.user.exception.UserInfraException;
import com.nashs.daily_log.domain.user.info.UserInfo;
import com.nashs.daily_log.domain.user.repository.UserRepository;
import com.nashs.daily_log.infra.user.entity.User;
import com.nashs.daily_log.infra.user.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.nashs.daily_log.infra.user.exception.UserInfraException.UserInfraExceptionCode.NO_SUCH_USER;

@Repository
@Transactional
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
                                          .sub(userInfo.getSub())
                                          .email(userInfo.getEmail())
                                          .provider(userInfo.getProvider())
                                          .emailVerified(userInfo.isEmailVerified())
                                          .username(userInfo.getUsername())
                                          .picture(userInfo.getPicture())
                                          .build())
                                          .toInfo();
    }

    @Override
    public UserInfo findBySub(final String sub) {
        return userJpaRepository.findBySub(sub)
                                .orElseThrow(() -> new UserInfraException(NO_SUCH_USER))
                                .toInfo();
    }
}
