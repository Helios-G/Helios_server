package com.helios.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication; // 이 임포트가 반드시 필요합니다!
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    private Key key;
    private final long tokenValidityInMilliseconds = 1000L * 60 * 60 * 10; // 10시간

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Base64.getEncoder().encode(secretKey.getBytes());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성
    public String createToken(String email, String role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMilliseconds);

        return Jwts.builder()
                .subject(email)
                .claim("auth", role)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key) // 최신 버전은 알고리즘 자동 선택
                .compact();
    }

    // 토큰 유효성 검증 (parserBuilder -> parser)
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith((javax.crypto.SecretKey) key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰에서 인증 정보 조회 (parserBuilder -> parser)
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String email = claims.getSubject();
        String role = claims.get("auth").toString();

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
        User principal = new User(email, "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}