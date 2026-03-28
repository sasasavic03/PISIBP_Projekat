package org.instagram.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret:my-super-secret-key-that-is-very-long-123456}")
    private final String SECRET =
            "my-super-secret-key-that-is-very-long-123456";

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String username, Long userId){

        return Jwts.builder()
                .setSubject(username)
                .claim("id", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}