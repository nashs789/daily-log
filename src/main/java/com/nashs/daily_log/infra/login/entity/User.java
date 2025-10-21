package com.nashs.daily_log.infra.login.entity;

import com.nashs.daily_log.domain.login.info.UserInfo;
import com.nashs.daily_log.infra.common.Timestamp;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public UserInfo toUserInfo() {
        return UserInfo.builder()
                       .id(id)
                       .sub(sub)
                       .email(email)
                       .provider(provider)
                       .emailVerified(emailVerified)
                       .username(username)
                       .picture(picture)
                       .build();
    }
}
