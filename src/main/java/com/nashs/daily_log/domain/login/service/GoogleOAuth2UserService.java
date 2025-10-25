package com.nashs.daily_log.domain.login.service;

import com.nashs.daily_log.domain.login.info.GoogleProfile;
import com.nashs.daily_log.domain.login.oauthPrincipal.GoogleOAuth2Principal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GoogleOAuth2UserService extends DefaultOAuth2UserService {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(req);
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        // Record 로 파싱
        GoogleProfile profile = GoogleProfile.fromAttributes(user.getAttributes());
        log.info("[OAuth] attrs={}", user.getAttributes());
        Map<String, Object> attrs = Map.of(
                "sub", profile.sub(),
                "email", profile.email(),
                "name", profile.name(),
                "picture", profile.picture()
        );
        // 여기서는 그대로 반환(필요 시 DB upsert 등 도메인 처리 추가)
        return new GoogleOAuth2Principal(profile, authorities, attrs);
    }
}
