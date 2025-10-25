package com.nashs.daily_log.domain.login.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String callback;

    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse res, AuthenticationException ex) throws IOException {
        log.info("callback = {}", callback);
        if (callback != null && !callback.isBlank()) {
            String loc = String.format("%s?error=%s", callback, URLEncoder.encode(ex.getMessage(), StandardCharsets.UTF_8));
            res.setStatus(302);
            res.setHeader("Location", loc);
        } else {
            res.sendError(401, "OAuth2 Login Failed: " + ex.getMessage());
        }
    }
}
