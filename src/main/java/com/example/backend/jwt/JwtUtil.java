package com.example.backend.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final String SECRET = "mysecretkeymysecretkeymysecretkey123456789";

    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public static String generateToken(String email, String role) {

        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims extractClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public static String getRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    public static boolean validateToken(String token) {

        try {

            extractClaims(token);

            return true;

        } catch (Exception e) {

            System.out.println("JWT validation failed: " + e.getMessage());
            e.printStackTrace();

            return false;
        }
    }

}