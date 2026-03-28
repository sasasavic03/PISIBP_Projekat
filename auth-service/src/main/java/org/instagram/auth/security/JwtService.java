package org.instagram.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.instagram.auth.config.JwtProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Key;
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
            Date now = new Date();
            Date expirationDate = new Date(now.getTime() + jwtProperties.getExpiration());

            String token = Jwts.builder()
                    .setSubject(username)
                    .claim("userId", userId)
                    .claim("type", TOKEN_TYPE)
                    .setIssuedAt(now)
                    .setExpiration(expirationDate)
                    .setIssuer(jwtProperties.getIssuer())
                    .signWith(signingKey, SignatureAlgorithm.HS256)
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
}