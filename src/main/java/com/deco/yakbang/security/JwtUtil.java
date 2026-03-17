package com.deco.yakbang.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import com.deco.yakbang.service.UserService;
import com.deco.yakbang.service.vo.UserVO;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class JwtUtil {
    // 주의: 실제 운영 환경에서는 보안을 위해 충분히 긴(32바이트 이상) 비밀키를 환경변수 등에서 불러와야 합니다.
    private static final String SECRET = "${jwt.secret}"; 
    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());
    private static final long ACCESS_EXPIRATION = 1000L * 60 * 10; // 10분
    private static final long REFRESH_EXPIRATION = 1000L * 60 * 60 * 24 * 7; // 7일
    
    @Resource(name = "userService") 
    private static UserService service;

    /**
     * 엑세스 토큰 생성
     */
    public static String generateAccessToken(UserVO user) {
        return Jwts.builder()
                .subject(String.valueOf(user.getId())) // int를 String으로 변환
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION))
                .signWith(key)
                .compact();
    }

    /**
     * 리프레시 토큰 생성
     */
    public static String generateRefreshToken(String userId) {
        return Jwts.builder()
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .signWith(key)
                .compact();
    }

    /**
     * 토큰에서 사용자 ID 추출
     */
    public static String getUserIdFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public static Key getKey() {
        return key;
    }

    /**
     * 🔄 refresh_token을 통해 새로운 access_token 발급
     */
    public static Map<String, String> refreshAccessToken(HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();
            String refreshToken = null;
            String baccessToken = null;
            
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("refresh_token".equals(cookie.getName())) refreshToken = cookie.getValue();
                    if ("access_token".equals(cookie.getName())) baccessToken = cookie.getValue();
                }
            }

            if (refreshToken == null || baccessToken == null) return null;

            Claims acclaims = null;
            try {
                // 만료된 토큰이라도 내부 페이로드를 가져오기 위해 파싱
                acclaims = Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(baccessToken)
                        .getPayload();
            } catch (ExpiredJwtException e) {
                acclaims = e.getClaims(); // 만료된 토큰에서 Claims 추출
            }

            // 리프레시 토큰 검증
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload();

            // 리프레시 토큰 만료 확인 (parseSignedClaims에서 예외가 안 났다면 유효한 것)
            String userId = claims.getSubject();
            
            // 새로운 access_token 발급을 위한 데이터 세팅
            UserVO dummyUser = new UserVO();

 
            String newAccessToken = generateAccessToken(dummyUser);

            Map<String, String> result = new HashMap<>();
            result.put("accessToken", newAccessToken);
            result.put("status", "success");
            return result;

        } catch (JwtException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 토큰 상태 확인 (유효/만료/없음)
     */
    public static String verifyToken(HttpServletRequest request) {
        String baccessToken = getAccessTokenFromRequest(request);
        
        if (baccessToken == null) return "null";
        
        try { 
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(baccessToken);
            return baccessToken;
        } catch (ExpiredJwtException e) {
            return "expired";
        } catch (JwtException e) {
            return "null";
        }
    }
    
    /**
     * 토큰 검증 후 ID 반환
     */
    public static String validateAndGetUserId(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
    
    public static String getFactionFromToken(HttpServletRequest request) {
        String token = getAccessTokenFromRequest(request);
        return getClaimFromToken(token, "faction");
    }
    
    public static String getTypeFromToken(String token) {
        return getClaimFromToken(token, "type");
    }
    
    public static String getInGameIdFromToken(String token) {
        return getClaimFromToken(token, "ingame_id");
    }
    
    // 중복 코드 방지를 위한 공통 클레임 추출 메소드
    private static String getClaimFromToken(String token, String claimName) {
        if (token == null) return null;
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            Object value = claims.get(claimName);
            return value != null ? String.valueOf(value) : null;
        } catch (Exception e) {
            return null;
        }
    }
    
    private static String getAccessTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) return cookie.getValue();
            }
        }
        return null;
    }
}