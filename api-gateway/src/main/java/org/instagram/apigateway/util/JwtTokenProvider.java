package org.instagram.apigateway.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.instagram.apigateway.config.JwtProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final JwtProperties jwtProperties;
    private final Key signingKey;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.signingKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        logger.info("JwtTokenProvider initialized with issuer: {} and algorithm: {}", 
                   jwtProperties.getIssuer(), 
                   jwtProperties.getAlgorithm());
    }


    public boolean validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            logger.warn("Token is null or empty");
            return false;
        }

        try {
            // Single parse operation that validates signature and format
            Jws<Claims> jws = getParser().parseClaimsJws(token);
            Claims claims = jws.getBody();
            
            // Check expiration without parsing again
            Date expirationDate = claims.getExpiration();
            if (expirationDate == null) {
                logger.warn("Expiration date is null in token");
                return false;
            }
            
            if (expirationDate.before(new Date())) {
                logger.debug("JWT token has expired. Expiration: {}", expirationDate);
                return false;
            }
            
            logger.debug("Token validated successfully");
            return true;
            
        } catch (SecurityException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token format: {}", e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            logger.debug("JWT token has expired: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error during token validation: {}", e.getMessage(), e);
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                logger.warn("Claims are null");
                return null;
            }
            String username = claims.getSubject();
            logger.debug("Extracted username from token: {}", username);
            return username;
        } catch (Exception e) {
            logger.error("Error extracting username from token: {}", e.getMessage());
            return null;
        }
    }

    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                logger.warn("Claims are null");
                return null;
            }
            Long userId = claims.get("userId", Long.class);
            logger.debug("Extracted userId from token: {}", userId);
            return userId;
        } catch (ClassCastException e) {
            logger.error("UserId is not a Long: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("Error extracting userId from token: {}", e.getMessage());
            return null;
        }
    }

    public Claims getClaimsFromToken(String token) {
        try {
            return getParser()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            logger.debug("Token is expired, but claims extracted: {}", e.getMessage());
            return e.getClaims(); // Return claims even if expired
        } catch (Exception e) {
            logger.error("Error extracting claims from token: {}", e.getMessage());
            return null;
        }
    }

    @Deprecated(forRemoval = true)
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                logger.warn("Cannot check expiration: claims are null");
                return true;
            }
            Date expirationDate = claims.getExpiration();
            if (expirationDate == null) {
                logger.warn("Expiration date is null in token");
                return true;
            }
            boolean isExpired = expirationDate.before(new Date());
            if (isExpired) {
                logger.debug("Token is expired. Expiration: {}", expirationDate);
            }
            return isExpired;
        } catch (ExpiredJwtException e) {
            logger.debug("Token is expired (caught ExpiredJwtException)");
            return true;
        } catch (Exception e) {
            logger.error("Error checking token expiration: {}", e.getMessage());
            return true; // Treat as expired on error for security
        }
    }

    @SuppressWarnings("unused")
    public Date getTokenExpirationDate(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims != null) {
                return claims.getExpiration();
            }
        } catch (Exception e) {
            logger.error("Error extracting expiration date from token: {}", e.getMessage());
        }
        return null;
    }

    @SuppressWarnings("unused")
    public String getTokenIssuer(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims != null) {
                return claims.getIssuer();
            }
        } catch (Exception e) {
            logger.error("Error extracting issuer from token: {}", e.getMessage());
        }
        return null;
    }

    private JwtParser getParser() {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .requireIssuer(jwtProperties.getIssuer())
                .build();
    }


    @SuppressWarnings("unused")
    protected Key getSigningKey() {
        return signingKey;
    }

    @SuppressWarnings("unused")
    public JwtProperties getJwtProperties() {
        return jwtProperties;
    }
}
