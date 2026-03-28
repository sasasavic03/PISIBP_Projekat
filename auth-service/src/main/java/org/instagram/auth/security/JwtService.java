package org.instagram.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.instagram.auth.config.JwtProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    private static final String TOKEN_TYPE = "Bearer";

    private final JwtProperties jwtProperties;
    private final Key signingKey;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.jwtProperties.validate();
        this.signingKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        logger.info("JwtService initialized with algorithm: {} and expiration: {}ms",
                jwtProperties.getAlgorithm(),
                jwtProperties.getExpiration());
    }

    public String generateToken(String username, Long userId) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("UserId must be a positive number");
        }

        try {
            Instant now = Instant.now();
            Instant expirationInstant = now.plusMillis(jwtProperties.getExpiration());

            String token = Jwts.builder()
                    .subject(username)
                    .claim("userId", userId)
                    .claim("type", TOKEN_TYPE)
                    .issuedAt(Date.from(now))
                    .expiration(Date.from(expirationInstant))
                    .issuer(jwtProperties.getIssuer())
                    .signWith(signingKey)
                    .compact();

            logger.debug("Token generated successfully for user: {} (ID: {})", username, userId);
            return token;
        } catch (Exception e) {
            logger.error("Error generating JWT token for user: {}", username, e);
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    protected Key getSigningKey() {
        return signingKey;
    }

    public JwtProperties getJwtProperties() {
        return jwtProperties;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((javax.crypto.SecretKey) signingKey)
                    .build()
                    .parseSignedClaims(token);
            logger.debug("Token validation successful");
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token is expired: {}", e.getMessage());
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public String refreshToken(String token) {
        try {
            Claims claims = extractClaims(token);
            String username = claims.getSubject();
            Long userId = claims.get("userId", Long.class);

            if (username == null || userId == null) {
                throw new RuntimeException("Invalid token: missing username or userId");
            }

            logger.debug("Token refreshed successfully for user: {} (ID: {})", username, userId);
            return generateToken(username, userId);
        } catch (Exception e) {
            logger.error("Error refreshing token: {}", e.getMessage());
            throw new RuntimeException("Failed to refresh token", e);
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Claims getAllClaimsFromToken(String token) {
        try {
            return extractClaims(token);
        } catch (Exception e) {
            logger.warn("Error extracting claims from token: {}", e.getMessage());
            return null;
        }
    }
}
