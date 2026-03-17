package com.deco.yakbang.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}") // application.yml의 설정을 통해 젠킨스 주입값을 가져옴
    private String secretString;

    private final long tokenValidity = 1000L * 60 * 60 * 10; // 10시간
    private SecretKey key;

    @PostConstruct
    protected void init() {
        // 젠킨스에서 주입된 문자열을 암호화 키 객체로 변환
        this.key = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String userId, String name) {
        Date now = new Date();
        return Jwts.builder()
                .subject(userId)
                .claim("name", name)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + tokenValidity))
                .signWith(key)
                .compact();
    }

    public String getUserId(String token) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}