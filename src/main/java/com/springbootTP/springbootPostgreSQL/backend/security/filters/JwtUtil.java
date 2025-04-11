package com.springbootTP.springbootPostgreSQL.backend.security.filters;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtil {

    // Clé secrète pour signer les tokens
    private static final byte[] SECRET_KEY_BYTES = "superSecretKeyForJWTGenerationAndValidation123!".getBytes(StandardCharsets.UTF_8);
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_BYTES);

    // Durée de validité des tokens
    private static final long ACCESS_TOKEN_VALIDITY = 1000L * 60; // 1 minute
    private static final long REFRESH_TOKEN_VALIDITY = 1000L * 60 *10;

    // Générer un token d'accès
    public String generateAccessToken(String username) {
        return generateToken(username, ACCESS_TOKEN_VALIDITY);
    }

    // Générer un refresh token
    public String generateRefreshToken(String username) {
        return generateToken(username, REFRESH_TOKEN_VALIDITY);
    }

    // Méthode privée pour générer un token avec une durée spécifique
    private String generateToken(String username, long validity) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // Valider un token
    public boolean validateToken(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            return extractedUsername.equals(username) && !isTokenExpired(token);
        } catch (JwtException e) {
            log.warn("JWT invalide : {}", e.getMessage());
            return false;
        }
    }

    // Extraire le nom d'utilisateur depuis un token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Vérifier si un token est expiré
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // Extraire un claim spécifique d'un token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extraire tous les claims d'un token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}