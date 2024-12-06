package com.example.demo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    private final SecretKey key;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    // 생성자를 통해 SecretKey를 초기화
    public JwtUtils(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * JWT 토큰 생성
     * @param username 사용자 이름
     * @return 생성된 JWT 토큰
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date()) // 현재 시간
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS512) // 키 및 알고리즘 설정
                .compact();
    }

    /**
     * JWT 토큰에서 사용자 이름 추출
     * @param token JWT 토큰
     * @return 사용자 이름
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * 토큰 유효성 검증
     * @param token JWT 토큰
     * @param userDetails 사용자 세부 정보
     * @return 유효 여부
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 토큰 만료 여부 확인
     * @param token JWT 토큰
     * @return 만료 여부
     */
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    /**
     * 모든 클레임 추출
     * @param token JWT 토큰
     * @return 클레임
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
