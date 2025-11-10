package com.nashs.daily_log.infra.user.entity;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.user.info.UserInfo;
import com.nashs.daily_log.infra.common.entity.Timestamp;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Entity
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends Timestamp {

    @Getter
    @RequiredArgsConstructor
    public enum Provider {
        GOOGLE,
        KAKAO,
        NAVER;
    }

    @Id
    @Column
    private String sub;

    @Column
    private String email;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column
    private boolean emailVerified;

    @Column
    private String username;

    @Column
    private String picture;

    public UserInfo toInfo() {
        return UserInfo.builder()
                       .sub(sub)
                       .email(email)
                       .provider(provider)
                       .emailVerified(emailVerified)
                       .username(username)
                       .picture(picture)
                       .build();
    }

    public static User ref(String userId) {
        return User.builder().sub(userId).build();
    }

    public static User fromInfo(UserInfo userInfo) {
        if (Objects.isNull(userInfo)) {
            return null;
        }

        return User.builder()
                   .sub(userInfo.getSub())
                   .build();
    }

    public static User fromLifeLogUser(LifeLogUser lifeLogUser) {
        return User.fromInfo(lifeLogUser.toUserInfo());
    }
}
