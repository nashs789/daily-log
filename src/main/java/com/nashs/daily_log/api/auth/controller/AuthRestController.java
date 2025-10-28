package com.nashs.daily_log.api.auth.controller;

import com.nashs.daily_log.domain.auth.props.AuthCookieProps;
import com.nashs.daily_log.domain.auth.service.JwtService;
import com.nashs.daily_log.global.utils.CookieUtils;
import com.nashs.daily_log.global.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {
    private final JwtService jwt;
    private final AuthCookieProps authCookieProps;

    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(HttpServletRequest req, HttpServletResponse res) {
        String refresh = CookieUtils.readCookieByName(req, authCookieProps.refreshName());
        if (refresh == null || !jwt.validateRefreshToken(refresh)) {
            return ResponseEntity.status(401).build();
        }
        String sub   = jwt.getSubject(refresh);
        String email = jwt.getEmail(refresh);
        String newAccess = jwt.createAccessToken(sub, email);

        ResponseCookie accessCookie = ResponseCookie.from(authCookieProps.accessName(), newAccess)
                                                    .httpOnly(true)
                                                    .secure(authCookieProps.secure())
                                                    .sameSite(authCookieProps.sameSite())
                                                    .domain(StringUtils.emptyToNull(authCookieProps.domain()))
                                                    .path("/")
                                                    .maxAge(Duration.ofMinutes(authCookieProps.accessMaxAgeMins()))
                                                    .build();
        res.addHeader("Set-Cookie", accessCookie.toString());

        return ResponseEntity.noContent().build();
    }
}
