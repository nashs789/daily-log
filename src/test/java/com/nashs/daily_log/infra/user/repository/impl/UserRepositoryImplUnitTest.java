package com.nashs.daily_log.infra.user.repository.impl;

import com.nashs.daily_log.domain.user.info.UserInfo;
import com.nashs.daily_log.infra.user.entity.User;
import com.nashs.daily_log.infra.user.exception.UserInfraException;
import com.nashs.daily_log.infra.user.repository.UserJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.nashs.daily_log.infra.user.exception.UserInfraException.UserInfraExceptionCode.NO_SUCH_USER;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplUnitTest {

    @Mock
    UserJpaRepository userJpaRepository;

    @InjectMocks
    UserRepositoryImpl userRepository;

    @Test
    @DisplayName("Unit: 이미 가입한 유저 확인")
    void isRegisteredUser() {
        // given
        final String USER_SUB = "user1";

        when(userJpaRepository.existsById(USER_SUB))
                .thenReturn(true);

        // when
        boolean res = userRepository.isRegisteredUser(USER_SUB);

        // then
        assertTrue(res);
        verify(userJpaRepository)
                .existsById(USER_SUB);
        verifyNoMoreInteractions(userJpaRepository);
    }

    @Test
    @DisplayName("Unit: 가입하지 않은 유저 확인")
    void isNotRegisteredUser() {
        // given
        final String USER_SUB = "user1";

        when(userJpaRepository.existsById(USER_SUB))
                .thenReturn(false);

        // when
        boolean res = userRepository.isRegisteredUser(USER_SUB);

        // then
        assertFalse(res);
        verify(userJpaRepository)
                .existsById(USER_SUB);
        verifyNoMoreInteractions(userJpaRepository);
    }

    @Test
    @DisplayName("Unit: 신규 유저 저장")
    void saveUser() {
        // given
        UserInfo userInfo = UserInfo.builder()
                                    .build();
        User user = User.builder()
                        .sub("user1")
                        .email("test@email.com")
                        .build();

        when(userJpaRepository.save(any()))
                .thenReturn(user);

        // when
        UserInfo savedUserInfo = userRepository.saveSocialUser(userInfo);

        // then
        assertNotNull(savedUserInfo);
        assertEquals(user.getSub(), savedUserInfo.getSub());
        assertEquals(user.getEmail(), savedUserInfo.getEmail());
        verify(userJpaRepository).save(any());
        verifyNoMoreInteractions(userJpaRepository);
    }

    @Test
    @DisplayName("Unit: 가입한 유저 조회")
    void findUserBySub() {
        // given
        final String USER_SUB = "user1";
        User user = User.builder()
                        .sub(USER_SUB)
                        .build();

        when(userJpaRepository.findBySub(USER_SUB))
                .thenReturn(Optional.of(user));

        // when
        UserInfo userInfo = userRepository.findBySub(USER_SUB);

        // then
        assertNotNull(userInfo);
        assertEquals(user.getSub(), userInfo.getSub());
        verify(userJpaRepository).findBySub(USER_SUB);
        verifyNoMoreInteractions(userJpaRepository);
    }

    @Test
    @DisplayName("Unit: 가입하지 않은 유저 조회")
    void findNotRegisteredUser() {
        // given
        final String NOT_EXISTED_USER_SUB = "user999-test-no-info";

        doThrow(new UserInfraException(NO_SUCH_USER))
                .when(userJpaRepository)
                .findBySub(NOT_EXISTED_USER_SUB);

        // when & then
        assertThatThrownBy(() -> userRepository.findBySub(NOT_EXISTED_USER_SUB))
                .isInstanceOf(UserInfraException.class);
        verify(userJpaRepository).findBySub(NOT_EXISTED_USER_SUB);
        verifyNoMoreInteractions(userJpaRepository);
    }
}