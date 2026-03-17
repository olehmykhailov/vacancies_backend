package com.olehmykhailov.vacancies.auth.jwt;

import com.olehmykhailov.vacancies.infrastructure.exceptions.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    private final String jwtAccessSecretKey;
    private final Long jwtAccessExpiration;

    public JwtService(
            @Value("${app.jwt.access.secretkey}") String jwtAccessSecretKey,
            @Value("${app.jwt.access.expiration}") Long jwtAccessExpiration) {
        this.jwtAccessSecretKey = jwtAccessSecretKey;
        this.jwtAccessExpiration = jwtAccessExpiration;
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtAccessSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + jwtAccessExpiration))
                .setIssuedAt(new Date())
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("ERR:INVALID_TOKEN");
        }
    }

    public String extractUsernameClaim(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsernameClaim(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

}
