package com.xcelerate.cafeManagementSystem.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtUtil {

    private final String SECRET_KEY;

    private final int TOKEN_VALIDITY = 1000 * 60 * 60 * 10;

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        this.SECRET_KEY = secretKey;
        System.out.println("JWT Secret Key (constructor): " + SECRET_KEY); // Debugging line
    }

    public String generateToken(String email, String role, String id) {
        System.out.println("JWT Secret Key: " + SECRET_KEY); // Debugging line
        if(SECRET_KEY == null || SECRET_KEY.isEmpty()) {
            throw new RuntimeException("JWT secret key is not set");
        }
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY))
                .claim("role", role)
                .claim("id", id)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractRole(String token){
        return extractClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String token, String email) {
        final String extractedEmail = extractClaims(token).getSubject();
        return (extractedEmail.equals(email) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());

    }

}
