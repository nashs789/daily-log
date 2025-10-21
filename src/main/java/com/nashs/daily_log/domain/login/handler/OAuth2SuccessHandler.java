package com.nashs.daily_log.domain.login.handler;

import com.nashs.daily_log.domain.login.info.GoogleProfile;
import com.nashs.daily_log.domain.login.oauthPrincipal.GoogleOAuth2Principal;
import com.nashs.daily_log.domain.login.service.JwtService;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwt;
    //@Value("${APP_CALLBACK_SCHEME:}")
    private String appScheme = "/"; // 예: myapp://oauth2/callback

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException {
        OAuth2User principal = (OAuth2User) auth.getPrincipal();
        String platform = ((OAuth2AuthenticationToken) auth).getAuthorizedClientRegistrationId();

        if ("google".equals(platform)) {
            // 메소드로 만들자
            log.info("profile = {}", principal.getAttributes());
        }

        // TODO: DB upsert(google sub→우리 userId 매핑). 지금은 sub를 그대로 userId로 사용(데모)
        String userId = "test"; // sub;
        String email = "test@test.com";

        String access = jwt.createAccessToken(userId, email);
        String refresh = jwt.createRefreshToken(userId);

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
