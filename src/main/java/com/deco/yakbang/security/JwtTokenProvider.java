package com.deco.yakbang.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collections;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtTokenProvider {

    // Access Token 유효시간: 10시간
    private final long accessTokenValidity = 1000L * 60 * 60 * 10;
    // Refresh Token 유효시간: 7일
    private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 7; 

    private SecretKey key;

    @PostConstruct
    protected void init() {
        // 1. 젠킨스가 주입한 시스템 환경 변수를 우선적으로 읽음
        String secret = System.getenv("JWT_SECRET_KEY");
        
        // 2. 환경 변수가 없을 경우를 대비한 32자 이상의 안전한 기본키 (서버 기동 보장)
        if (secret == null || secret.isEmpty()) {
            secret = "yakbang_default_secure_secret_key_32_chars_2026";
            System.err.println("⚠️ [JWT] 환경변수 JWT_SECRET_KEY를 찾을 수 없어 기본키를 사용합니다.");
        }
        
        // 3. 키 객체 생성
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /** * Access Token 생성 (10시간) */
    public String createToken(String userId, String name) {
        Date now = new Date();
        return Jwts.builder()
                .subject(userId)
                .claim("name", name)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + accessTokenValidity))
                .signWith(key)
                .compact();
    }

    /** * Refresh Token 생성 (7일) - 이 부분이 추가되었습니다! */
    public String createRefreshToken(String userId) {
        Date now = new Date();
        return Jwts.builder()
                .subject(userId)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + refreshTokenValidity))
                .signWith(key)
                .compact();
    }

    /** * 토큰에서 유저 ID 추출 */
    public String getUserId(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /** * 토큰 유효성 및 만료 여부 확인 */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰이 위조되었거나 만료되었을 경우 false 반환
            return false;
        }
    }
    
    public Authentication getAuthentication(String token) {
        // 1. 토큰을 복호화해서 Claims(내용물)를 꺼냅니다.
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // 2. 권한 정보가 지금은 따로 없으므로 빈 리스트로 처리합니다.
        // (나중에 관리자/일반유저 구분이 필요하면 여기서 처리합니다.)
        UserDetails principal = new User(claims.getSubject(), "", Collections.emptyList());

        // 3. 인증 객체를 리턴 (principal: 유저정보, "": 비밀번호(불필요), 권한리스트)
        return new UsernamePasswordAuthenticationToken(principal, token, Collections.emptyList());
    }
}