package com.hisabKitab.springProject.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hisabKitab.springProject.exception.InvalidTokenException;
import com.hisabKitab.springProject.exception.TokenExpiredException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtUtil {

	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @PostConstruct
    public void testLogger() {
        logger.info("Logger is working in JwtUtil!");
    }

    private SecretKey getSigningKey() {
        // Ensure your secret key is at least 32 characters long for HS256
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), Jwts.SIG.HS256) // Explicitly set HS256
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
        try {
            logger.info("Extracting username from token");
            return getClaims(token).getSubject();
        } catch (ExpiredJwtException ex) {
            logger.error("Token expired: {}", ex.getMessage());
            throw new TokenExpiredException("Your session has expired. Please log in again.");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid token format: {}", ex.getMessage());
            throw new InvalidTokenException("Invalid token format. Please provide a valid token.");
        } catch (io.jsonwebtoken.security.SecurityException ex) {
            logger.error("Invalid token signature: {}", ex.getMessage());
            throw new InvalidTokenException("Invalid token signature. Please provide a valid token.");
        } catch (IllegalArgumentException ex) {
            logger.error("Token is missing: {}", ex.getMessage());
            throw new InvalidTokenException("Token is missing. Please provide a token.");
        }
    }
}
