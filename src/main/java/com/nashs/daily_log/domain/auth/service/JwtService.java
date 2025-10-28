package com.nashs.daily_log.domain.auth.service;

import com.nashs.daily_log.domain.auth.props.JwtProps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtService {
    private final JwtProps props;
    private final SecretKey key;

    public JwtService(JwtProps props) {
        this.props = props;
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
                   .claims(Map.of("email", email, "typ", "access"))
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
                   .claims(Map.of("typ", "refresh"))
                   .signWith(key)
                   .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            Claims c = parse(token);
            return "access".equals(c.get("typ", String.class));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            Claims c = parse(token);
            return "refresh".equals(c.get("typ", String.class));
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
