package com.nashs.daily_log.infra.user.repository.impl;

import com.nashs.daily_log.ContainerTest;
import com.nashs.daily_log.domain.user.info.UserInfo;
import com.nashs.daily_log.infra.user.exception.UserInfraException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.nashs.daily_log.infra.user.entity.User.Provider.GOOGLE;
import static com.nashs.daily_log.infra.user.exception.UserInfraException.UserInfraExceptionCode.NO_SUCH_USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@Sql(scripts = "/test-data/user/user.sql"
    , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    , config = @SqlConfig(encoding = "UTF-8"))
@Sql(scripts = "/test-data/user/user_cleanup.sql"
    , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    , config = @SqlConfig(encoding = "UTF-8"))
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
        // given
        final String NOT_EXISTED_USER_SUB = "user4";

        // when & then
        assertFalse(userRepository.isRegisteredUser(NOT_EXISTED_USER_SUB));
    }

    @Test
    @DisplayName("신규 유저 저장")
    void saveUser() {
        // given
        final String NOT_EXISTED_USER_SUB = "user4";

        // when
        UserInfo userInfo = userRepository.saveSocialUser(UserInfo.builder()
                                                                  .sub(NOT_EXISTED_USER_SUB)
                                                                  .provider(GOOGLE)
                                                                  .build());
        // then
        assertAll(() -> {
            assertEquals(NOT_EXISTED_USER_SUB, userInfo.getSub());
            assertEquals(GOOGLE, userInfo.getProvider());
        });
    }

    @Test
    @DisplayName("가입한 유저 조회")
    void findUserBySub() {
        // given
        final String USER_SUB = "user1";

        // when
        UserInfo userInfo = userRepository.findBySub(USER_SUB);

        // then
        assertEquals(USER_SUB, userInfo.getSub());
    }

    @Test
    @DisplayName("가입하지 않은 유저 조회")
    void findNotRegisteredUser() {
        // given & when
        UserInfraException userInfraException = assertThrows(UserInfraException.class, () -> userRepository.findBySub("user999-test-no-info"));
        // then
        assertEquals(UserInfraException.class, userInfraException.getClass());
        assertEquals(NOT_FOUND, userInfraException.getStatus());
        assertEquals(NO_SUCH_USER.getMsg(), userInfraException.getMessage());
    }
}