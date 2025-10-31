package com.nashs.daily_log.domain.auth.service;

import com.nashs.daily_log.domain.auth.props.AuthCookieProps;
import com.nashs.daily_log.domain.auth.props.JwtProps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtService {
    private final AuthCookieProps authCookieProps;
    private final JwtProps props;
    private SecretKey key;

    @PostConstruct
    void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(props.secret()));
    }

    public String createAccessToken(String sub, String email) {
        Instant now = Instant.now();
        Instant exp = now.plus(Duration.ofMinutes(props.accessExpMinutes()));
        return Jwts.builder()
                   .issuer(props.issuer())
                   .subject(sub)
                   .issuedAt(Date.from(now))
                   .expiration(Date.from(exp))
                   .claims(Map.of(
                           "email", email,
                           "typ", authCookieProps.accessName())
                   )
                   .signWith(key)
                   .compact();
    }

    public String createRefreshToken(String sub) {
        Instant now = Instant.now();
        Instant exp = now.plus(Duration.ofDays(props.refreshExpDays()));
        return Jwts.builder()
                   .issuer(props.issuer())
                   .subject(sub)
                   .issuedAt(Date.from(now))
                   .expiration(Date.from(exp))
                   .claims(Map.of("typ", authCookieProps.refreshName()))
                   .signWith(key)
                   .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            return authCookieProps.accessName()
                                  .equals(parse(token).get("typ", String.class));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            return authCookieProps.refreshName()
                                  .equals(parse(token).get("typ", String.class));
        } catch (Exception e) {
            return false;
        }
    }

    public String getSubject(String token) {
        return parse(token).getSubject();
    }

    public String getEmail(String token) {
        return parse(token).get("email", String.class);
    }

    public String getUserId(String token) {
        return parse(token).get("id", String.class);
    }

    private Claims parse(String token) {
        // 만료/서명/형식 에러 시 예외 발생
        return Jwts.parser()
                   .requireIssuer(props.issuer())
                   .verifyWith(key)
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }
}
