package com.nashs.daily_log.domain.login.handler;

import com.nashs.daily_log.domain.login.info.GoogleProfile;
import com.nashs.daily_log.domain.login.info.UserInfo;
import com.nashs.daily_log.domain.login.oauthPrincipal.GoogleOAuth2Principal;
import com.nashs.daily_log.domain.login.repository.UserRepository;
import com.nashs.daily_log.domain.login.service.JwtService;
import com.nashs.daily_log.infra.login.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwt;
    //@Value("${APP_CALLBACK_SCHEME:}")
    private String appScheme = "/"; // 예: myapp://oauth2/callback

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException {
        OAuth2User principal = (OAuth2User) auth.getPrincipal();
        String platform = ((OAuth2AuthenticationToken) auth).getAuthorizedClientRegistrationId();

        if ("google".equals(platform)) {
            log.info("profile = {}", principal.getAttributes());
        }

        // ##### 임시 코드 (좋은 방법 찾기)#####
        Map<String, Object> attributes = principal.getAttributes();

        String sub = (String) attributes.get("sub");
        Long id = (Long) attributes.get("id");
        String email = (String) attributes.get("email");
        Boolean emailVerified = (Boolean) attributes.get("email_verified");
        String username = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");
        // ##### 임시 코드 #####

        if (!userRepository.isRegisteredUser(sub)) {
            UserInfo userInfo = userRepository.saveSocialUser(UserInfo.builder()
                                                                      .id(id)
                                                                      .sub(sub)
                                                                      .email(email)
                                                                      .emailVerified(emailVerified)
                                                                      .provider(User.Provider.GOOGLE)
                                                                      .username(username)
                                                                      .picture(picture)
                                                                      .build());
            log.info("신규 등록 = {}", userInfo);
        }

        String access = jwt.createAccessToken(sub, email);
        String refresh = jwt.createRefreshToken(sub);

        if (appScheme != null && !appScheme.isBlank()) {
            // 모바일 딥링크로 리다이렉트
            String loc = String.format("%s?access=%s&refresh=%s", appScheme, URLEncoder.encode(access, StandardCharsets.UTF_8),
                                       URLEncoder.encode(refresh, StandardCharsets.UTF_8));
            res.setStatus(302);
            res.setHeader("Location", loc);
        } else {
            // 임시 페이지로 토큰 표시
            res.setContentType("text/html; charset=UTF-8");
            res.getWriter().printf("""
        <html><body>
          <h3>Login Success</h3>
          <p><b>access</b>: %s</p>
          <p><b>refresh</b>: %s</p>
          <p>나중에 APP_CALLBACK_SCHEME(예: myapp://oauth2/callback)을 설정하면 앱으로 바로 이동합니다.</p>
        </body></html>
      """, access, refresh);
        }
    }
}
