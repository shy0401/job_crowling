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

    @Value("${jwt.expiration:86400000}") // 기본값 설정
    private long jwtExpirationMs;

    public JwtUtils(@Value("${jwt.secret:defaultsecretkey}") String encodedKey) {
        this.key = createKey(encodedKey);
    }

    // 키 생성 로직을 별도로 분리
    private SecretKey createKey(String encodedKey) {
        try {
            if (encodedKey == null || encodedKey.isEmpty()) {
                throw new IllegalArgumentException("Encoded key is null or empty");
            }

            // Base64 디코딩
            byte[] decodedKey = Base64.getDecoder().decode(encodedKey);

            // 디코딩된 키 길이 확인 (256비트 이상이어야 함)
            if (decodedKey.length < 32) { // 32바이트 = 256비트
                throw new IllegalArgumentException("Key length is less than 256 bits");
            }

            return Keys.hmacShaKeyFor(decodedKey);
        } catch (Exception e) {
            // 예외 발생 시 기본 키 반환
            return Keys.secretKeyFor(SignatureAlgorithm.HS512);
        }
    }

    // JWT 토큰 생성
    public String generateToken(String username) {
        try {
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        } catch (Exception e) {
            // 예외 발생 시 null 반환
            return null;
        }
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
            // 예외 발생 시 null 반환
            return null;
        }
    }

    // JWT 토큰 유효성 검사
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);
            return username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // JWT 만료 여부 확인
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
            return false;
        }
    }
}
