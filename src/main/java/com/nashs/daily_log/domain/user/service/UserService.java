package com.nashs.daily_log.domain.user.service;

import com.nashs.daily_log.domain.user.info.UserInfo;
import com.nashs.daily_log.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean isRegisteredUser(String sub) {
        return userRepository.isRegisteredUser(sub);
    }

    public UserInfo saveSocialUser(UserInfo userInfo) {
        return userRepository.saveSocialUser(userInfo);
    }

    public UserInfo findBySub(String sub) {
        return userRepository.findBySub(sub);
    }
}
