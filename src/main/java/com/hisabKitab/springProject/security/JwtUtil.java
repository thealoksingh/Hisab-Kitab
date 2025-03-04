package com.hisabKitab.springProject.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expirationInMs}")
    private long expirationInMs;

    @PostConstruct
    public void testLogger() {
        logger.info("Logger is working in JwtUtil!");
    }

    private SecretKey getSigningKey() {
        // Ensure your secret key is at least 32 characters long for HS256
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateTokenByIdAndRole(Long userId, String role) {
        return Jwts.builder()
                .issuer("HisabKitab")
                .subject(String.valueOf(userId)) // UserId as subject
                .claim("roles", role) // Setting roles as claims
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationInMs)) // 1 day in milliseconds
                // .signWith(getSigningKey(),
                // Jwts.SIG.HS256)
                .signWith(getSigningKey(), Jwts.SIG.HS256) // Sign with the key
                .compact();
    }

    public String generateToken(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(""));

        return Jwts.builder()
                .issuer("HisabKitab")
                .subject(String.valueOf(userDetails.getUser().getUserId())) // UserId as subject
                .claim("roles", roles) // Setting roles as claims
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationInMs))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = getClaims(token).getSubject();
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    public String getUsername(String token) {

        return getClaims(token).getSubject();

    }
}
