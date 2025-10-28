package com.nashs.daily_log.global.filter;

import com.nashs.daily_log.domain.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtCookieAuthFilter extends OncePerRequestFilter {
    private final JwtService jwt;
    private final String accessCookieName;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        log.info("@@@@@@@@@@@@");
        // 이미 인증돼 있으면 패스
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = readCookie(req, accessCookieName);

            if (StringUtils.hasText(token) && jwt.validateAccessToken(token)) {
                String sub = jwt.getSubject(token);
                // 권한 로딩 정책에 맞추어 조정
                var auth = new UsernamePasswordAuthenticationToken(sub, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(req, res);
    }

    private static String readCookie(HttpServletRequest req, String name) {
        Cookie[] cs = req.getCookies();
        if (cs == null) return null;

        for (Cookie c : cs) {
            if (name.equals(c.getName())) {
                return c.getValue();
            }
        }

        return null;
    }
}
