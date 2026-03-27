package org.instagram.apigateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;

@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);

    private String secret;
    private long expiration = 86400000; // 24 hours default
    private String algorithm = "HS256";
    private String issuer = "instagram-auth-service";

    public JwtProperties() {
    }

    public JwtProperties(String secret, long expiration, String algorithm, String issuer) {
        this.secret = secret;
        this.expiration = expiration;
        this.algorithm = algorithm;
        this.issuer = issuer;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    @PostConstruct
    public void init() {
        validate();
        logger.info("JWT properties validated successfully");
    }

    public void validate() {
        if (secret == null || secret.trim().isEmpty()) {
            throw new IllegalArgumentException("JWT secret must be configured via JWT_SECRET environment variable or jwt.secret property");
        }
        if (secret.length() < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 characters long for HS256 algorithm");
        }
        if (expiration <= 0) {
            throw new IllegalArgumentException("JWT expiration must be greater than 0");
        }
    }
}

