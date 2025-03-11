package com.springbootTP.springbootPostgreSQL.backend.security.filters;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Clé secrète sous forme de tableau de bytes pour éviter les erreurs de conversion
    private static final byte[] SECRET_KEY_BYTES = "superSecretKeyForJWTGenerationAndValidation123!".getBytes(StandardCharsets.UTF_8);
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET_KEY_BYTES);

    // Durée de validité des tokens
    private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 60; // 1 heure
    private static final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24 * 7; // 7 jours

    // Génération du token d'accès
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, ACCESS_TOKEN_VALIDITY);
    }

    // Génération du token de rafraîchissement
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, REFRESH_TOKEN_VALIDITY);
    }

    // Méthode privée pour générer un token avec une durée spécifique
    private String generateToken(UserDetails userDetails, long expirationTime) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // Validation du token
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            return getUsernameFromToken(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (JwtException e) {
            System.out.println("JWT invalide : " + e.getMessage());
            return false;
        }
    }

    // Récupérer username depuis le token
    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Vérifier si le token est expiré
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // Extraire un claim spécifique du token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}