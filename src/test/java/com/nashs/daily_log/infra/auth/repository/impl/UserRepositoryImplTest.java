package com.nashs.daily_log.infra.auth.repository.impl;

import com.nashs.daily_log.ContainerTest;
import com.nashs.daily_log.infra.user.exception.UserInfraException;
import com.nashs.daily_log.domain.user.info.UserInfo;
import com.nashs.daily_log.infra.user.entity.User;
import com.nashs.daily_log.infra.user.repository.impl.UserRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Sql(scripts = "/testdata/user/user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/testdata/user/user_cleanup.sql",   executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserRepositoryImplTest extends ContainerTest {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Test
    @DisplayName("이미 가입한 유저인지 확인")
    void isRegisteredUser() {
        // given & when & then
        assertTrue(
                userRepository.isRegisteredUser("user1")
                && userRepository.isRegisteredUser("user2")
                && userRepository.isRegisteredUser("user3")
        );
    }

    @Test
    @DisplayName("가입하지 않은 유저인지 확인")
    void isNotRegisteredUser() {
        // given & when & then
        assertFalse(userRepository.isRegisteredUser("user4"));
    }

    @Test
    @DisplayName("신규 유저 저장")
    void saveUser() {
        // given & when
        UserInfo userInfo = userRepository.saveSocialUser(UserInfo.builder()
                                                                  .sub("user4")
                                                                  .provider(User.Provider.GOOGLE)
                                                                  .build());
        // then
        assertAll(() -> {
            assertEquals("user4", userInfo.getSub());
            assertEquals(User.Provider.GOOGLE, userInfo.getProvider());
        });
    }

    @Test
    @DisplayName("가입한 유저 조회")
    void findUserBySub() {
        // given & when
        UserInfo userInfo = userRepository.findBySub("user1");
        // then
        assertEquals("user1", userInfo.getSub());
    }

    @Test
    @DisplayName("가입하지 않은 유저 조회")
    void findNotRegisteredUser() {
        // given & when
        UserInfraException userInfraException = assertThrows(UserInfraException.class, () -> userRepository.findBySub("user999-test-no-info"));
        // then
        assertEquals(UserInfraException.class, userInfraException.getClass());
        assertEquals(NOT_FOUND, userInfraException.getStatus());
        assertEquals("존재하지 않는 유저 입니다.", userInfraException.getMessage());
    }
}