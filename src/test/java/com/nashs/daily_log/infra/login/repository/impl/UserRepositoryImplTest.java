package com.nashs.daily_log.infra.login.repository.impl;

import com.nashs.daily_log.ContainerTest;
import com.nashs.daily_log.domain.login.info.UserInfo;
import com.nashs.daily_log.infra.login.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Sql(scripts = "/testdata/user/user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/testdata/user/user_cleanup.sql",   executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserRepositoryImplTest extends ContainerTest {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Test
    void 이미_가입한_유저_확인() {
        // when & then
        assertTrue(
                userRepository.isRegisteredUser("user1")
                && userRepository.isRegisteredUser("user2")
                && userRepository.isRegisteredUser("user3")
        );
    }

    @Test
    void 가입하지_않은_유저() {
        // when & then
        assertFalse(userRepository.isRegisteredUser("user4"));
    }

    @Test
    void 유저_저장() {
        // given
        UserInfo userInfo = userRepository.saveSocialUser(UserInfo.builder()
                                                                  .sub("user4")
                                                                  .provider(User.Provider.GOOGLE)
                                                                  .build());
        // when & then
        assertAll(() -> {
            assertEquals("user4", userInfo.getSub());
            assertEquals(User.Provider.GOOGLE, userInfo.getProvider());
        });
    }
}