package com.example.productorderservice.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
public class JwtUtil {

    private final String SECRET = "mysecretkeymysecretkeymysecretkey123";
    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
    private final long EXPIRATION = 1000 * 60 * 60; // 1 hour

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        
        // Add roles based on username
        if ("admin".equals(username)) {
            claims.put("roles", Arrays.asList("ROLE_ADMIN", "ROLE_USER"));
            claims.put("authorities", Arrays.asList("READ_ORDERS", "WRITE_ORDERS", "DELETE_ORDERS"));
        } else {
            claims.put("roles", Arrays.asList("ROLE_USER"));
            claims.put("authorities", Arrays.asList("READ_ORDERS"));
        }
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Extract roles from token
    public List<String> extractRoles(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) claims.get("roles");
            return roles != null ? roles : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    // Extract authorities from token
    public List<String> extractAuthorities(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            @SuppressWarnings("unchecked")
            List<String> authorities = (List<String>) claims.get("authorities");
            return authorities != null ? authorities : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}