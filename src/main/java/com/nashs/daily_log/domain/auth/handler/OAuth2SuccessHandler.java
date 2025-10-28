package com.nashs.daily_log.domain.auth.handler;

import com.nashs.daily_log.domain.auth.info.GoogleAttrs;
import com.nashs.daily_log.domain.auth.parser.GoogleAttributeParser;
import com.nashs.daily_log.domain.auth.props.AuthCookieProps;
import com.nashs.daily_log.domain.auth.service.JwtService;
import com.nashs.daily_log.domain.user.info.UserInfo;
import com.nashs.daily_log.domain.user.repository.UserRepository;
import com.nashs.daily_log.global.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String appScheme;

    private final AuthCookieProps cookieProps;
    private final JwtService jwt;
    private final UserRepository userRepository;
    private final GoogleAttributeParser googleAttributeParser;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException {
        OAuth2User principal = (OAuth2User) auth.getPrincipal();
        String platform = ((OAuth2AuthenticationToken) auth).getAuthorizedClientRegistrationId();

        if ("google".equals(platform)) {
            log.info("google login");
        }

        GoogleAttrs attrs = googleAttributeParser.fromPrincipal(principal);
        String sub = attrs.sub();
        String email = attrs.email();
        UserInfo userInfo = userRepository.isRegisteredUser(sub)
                          ? userRepository.findBySub(sub)
                          : userRepository.saveSocialUser(attrs.toUserInfo());

        // TODO - Redis 로 이전 예정 + 토큰
        log.info("userInfo = {}", userInfo);

        String access = jwt.createAccessToken(sub, email);
        String refresh = jwt.createRefreshToken(sub);

        // TODO - token DB 저장

        ResponseCookie accessCookie = ResponseCookie.from(StringUtils.nullSafe(cookieProps.accessName(), "ACCESS"), access)
                                                    .httpOnly(true)
                                                    .secure(cookieProps.secure())
                                                    .sameSite(StringUtils.nullSafe(cookieProps.sameSite(), "Lax"))
                                                    .domain(StringUtils.nullOrTrim(cookieProps.domain()))      // 도메인 설정 없으면 생략
                                                    .path("/")
                                                    .maxAge(Duration.ofMinutes(cookieProps.accessMaxAgeMins()))
                                                    .build();
        ResponseCookie refreshCookie = ResponseCookie.from(StringUtils.nullSafe(cookieProps.refreshName(), "REFRESH"), refresh)
                                                     .httpOnly(true)
                                                     .secure(cookieProps.secure())
                                                     .sameSite(StringUtils.nullSafe(cookieProps.sameSite(), "Lax"))
                                                     .domain(StringUtils.nullOrTrim(cookieProps.domain()))
                                                     .path("/auth/refresh")
                                                     .maxAge(Duration.ofDays(cookieProps.refreshMaxAgeDays()))
                                                     .build();

        res.addHeader("Set-Cookie", accessCookie.toString());
        res.addHeader("Set-Cookie", refreshCookie.toString());
        res.setStatus(302);
        res.setHeader("Location", ServletUriComponentsBuilder.fromCurrentContextPath()
                                                                         .build()
                                                                         .toUriString() + "/");
    }
}
