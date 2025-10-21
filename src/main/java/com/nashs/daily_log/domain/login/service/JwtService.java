package com.nashs.daily_log.domain.login.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtService {
    //@Value("${APP_JWT_SECRET:dev-secret-change-me}")
    private String secret = "change-this-to-a-long-random-secret";

    public String createAccessToken(String userId, String email) {
        Instant now = Instant.now();
        return Jwts.builder()
                   .subject(userId)
                   .claim("email", email)
                   .issuedAt(Date.from(now))
                   .expiration(Date.from(now.plus(Duration.ofMinutes(30))))
                   .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                   .compact();
    }

    public String createRefreshToken(String userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                   .subject(userId)
                   .claim("typ", "refresh")
                   .issuedAt(Date.from(now))
                   .expiration(Date.from(now.plus(Duration.ofDays(14))))
                   .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                   .compact();
    }
}
