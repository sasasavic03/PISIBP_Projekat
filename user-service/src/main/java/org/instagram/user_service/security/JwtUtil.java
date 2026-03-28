package org.instagram.user_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtUtil {

    private final String SECRET = "my-super-secret-key-that-is-very-long-123456";

    public Long extractUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();

        return Long.valueOf(claims.get("id").toString());
    }
}