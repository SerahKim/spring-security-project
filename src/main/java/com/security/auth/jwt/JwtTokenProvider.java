package com.security.auth.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;
    @Value("${jwt.secret}")
    private String secret;

    // Acess Token 만료시간 (분)
    @Value("${jwt.access-token-validity-in-seconds}")
    private long accessTokenValidityInSeconds;

    // Refresh Token 만료시간 (일)
    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;

    private SecretKey secretKey;

    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    public void init() {
        // Base64로 인코딩된 문자열 -> 바이트 배열로 디코딩
        byte[] keyBytes = Base64.getDecoder().decode(secret);

        // 디코딩된 바이트 배열 -> SecretKey 객체를 생성(HMAC SHA 알고리즘 사용)
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateToken(Long userId, long validityInSeconds) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(validityInSeconds);

        return Jwts.builder()
                .subject(userId.toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact();
    }

    // Access Token 생성
    public String generateAccessToken(Long userId) {
        return generateToken(userId, accessTokenValidityInSeconds);
    }

    // Refresh Token 생성
    public String generateRefreshToken(Long userId) {
        return generateToken(userId, refreshTokenValidityInSeconds);
    }

    // 헤더에서 토큰 꺼내기
    // request header => Authorization: Bearer accessToken
    public String getToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");

        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }

        return null;
    }

    // 토큰에서 userId 추출
    public Long getUserIdFromToken(String token) {
        return Long.parseLong(Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject()
        );
    }

    // userId 기반 Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        Long userId = getUserIdFromToken(token);

        // userId를 통해 사용자 정보 로드
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId.toString());

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰 유효성 검사
    public void validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
        } catch (io.jsonwebtoken.security.SecurityException | io.jsonwebtoken.MalformedJwtException e) {
            throw new JwtException("잘못된 JWT 서명입니다.");
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new JwtException("만료된 JWT 토큰입니다.");
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            throw new JwtException("지원하지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new JwtException("JWT 토큰이 비어있거나 잘못되었습니다.");
        }
    }
}

