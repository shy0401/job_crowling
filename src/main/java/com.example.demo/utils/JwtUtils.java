package com.example.demo.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {

    private final SecretKey key;

    @Value("${jwt.expiration:86400000}") // 기본 만료 시간: 1일
    private long jwtExpirationMs;

    public JwtUtils(@Value("${jwt.secret:defaultsecretkey}") String encodedKey) {
        this.key = createKey(encodedKey);
    }

    // 비밀키 생성
    private SecretKey createKey(String encodedKey) {
        try {
            if (encodedKey == null || encodedKey.isEmpty()) {
                throw new IllegalArgumentException("Encoded key is null or empty");
            }

            byte[] decodedKey = Base64.getDecoder().decode(encodedKey);

            if (decodedKey.length < 32) { // 32바이트 이상이어야 HS256 또는 HS512에 적합
                throw new IllegalArgumentException("Key length is less than 256 bits");
            }

            return Keys.hmacShaKeyFor(decodedKey);
        } catch (Exception e) {
            // 기본 키 생성 (보안성을 보장하기 위해 Key 생성 실패 시 예외를 기록)
            System.err.println("Key creation failed, using default key: " + e.getMessage());
            return Keys.secretKeyFor(SignatureAlgorithm.HS512);
        }
    }

    // JWT 토큰 생성
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // JWT에서 사용자 이름 추출
    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            System.err.println("Failed to extract username: " + e.getMessage());
            return null;
        }
    }

    // JWT 토큰 유효성 검사
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (JwtException e) {
            System.err.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    // 토큰의 만료 여부 확인
    private boolean isTokenExpired(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (JwtException e) {
            System.err.println("Token expiration check failed: " + e.getMessage());
            return true; // 기본적으로 만료된 것으로 간주
        }
    }
}
