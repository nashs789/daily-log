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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "/test-data/user/user_insert.sql"
    , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    , config = @SqlConfig(encoding = "UTF-8"))
@Sql(scripts = "/test-data/user/user_cleanup.sql"
    , executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    , config = @SqlConfig(encoding = "UTF-8"))
class UserRepositoryImplTest extends ContainerTest {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Test
    @DisplayName("이미 가입한 유저 확인")
    void isRegisteredUser() {
        // given & when & then
        assertTrue(
                userRepository.isRegisteredUser("user1")
                        && userRepository.isRegisteredUser("user2")
                        && userRepository.isRegisteredUser("user3")
        );
    }

    @Test
    @DisplayName("가입하지 않은 유저 확인")
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
        UserInfo userInfo = userRepository.saveSocialUser(UserInfo.ref(NOT_EXISTED_USER_SUB));
        // then
        assertThat(userInfo)
                .isNotNull()
                .extracting(UserInfo::getSub)
                .isEqualTo(NOT_EXISTED_USER_SUB);
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
        // given
        final String NOT_EXISTED_USER_SUB = "user999-test-no-info";

        // when & then
        assertThatThrownBy(() -> userRepository.findBySub(NOT_EXISTED_USER_SUB))
                .isInstanceOf(UserInfraException.class)
                .extracting("status")
                .isEqualTo(NOT_FOUND);
    }
}