package com.nashs.daily_log.domain.login.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {
    //@Value("${APP_CALLBACK_SCHEME:}")
    private String appScheme = "myapp://oauth2/callback";

    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex) throws IOException {
        if (appScheme != null && !appScheme.isBlank()) {
            String loc = String.format("%s?error=%s", appScheme, URLEncoder.encode(ex.getMessage(), StandardCharsets.UTF_8));
            res.setStatus(302);
            res.setHeader("Location", loc);
        } else {
            res.sendError(401, "OAuth2 Login Failed: " + ex.getMessage());
        }
    }
}
