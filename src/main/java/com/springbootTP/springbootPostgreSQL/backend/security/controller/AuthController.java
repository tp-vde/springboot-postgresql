package com.springbootTP.springbootPostgreSQL.backend.security.controller;

import com.springbootTP.springbootPostgreSQL.backend.security.filters.JwtUtil;
import com.springbootTP.springbootPostgreSQL.backend.security.model.Token;
import com.springbootTP.springbootPostgreSQL.backend.security.model.Users;
import com.springbootTP.springbootPostgreSQL.backend.security.service.JwtServiceInterface;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final JwtServiceInterface jwtServiceInterface;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(JwtServiceInterface jwtServiceInterface, JwtUtil jwtUtil,
                          AuthenticationManager authenticationManager) {
        this.jwtServiceInterface = jwtServiceInterface;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Authentification et génération des tokens
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {
        log.info("Tentative de connexion pour l'utilisateur avec l'email : {}", user.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            UserDetails userDetails = jwtServiceInterface.loadUserByUsername(user.getEmail());
            String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername());
            String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

            log.info("Connexion réussie pour l'utilisateur avec l'email : {}", user.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Connexion réussie. Voici vos tokens.");
            response.put("access_token", accessToken);
            response.put("refresh_token", refreshToken);

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            log.warn("Échec de la connexion pour l'utilisateur avec l'email : {}. Motif : Email ou mot de passe incorrect.", user.getEmail());
            return ResponseEntity.status(401).body("Email ou mot de passe incorrect !");
        } catch (Exception e) {
            log.error("Erreur inattendue lors de la connexion pour l'utilisateur avec l'email : {}. Message : {}", user.getEmail(), e.getMessage());
            return ResponseEntity.status(500).body("Erreur interne du serveur.");
        }
    }

    /**
     * Rafraîchir le token d'accès avec le refresh token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Token tokenRequest) {
        log.info("Tentative de rafraîchissement du token.");

        try {
            String refreshToken = tokenRequest.getRefreshToken();

            if (refreshToken == null || refreshToken.isEmpty()) {
                log.warn("Échec du rafraîchissement du token : Refresh token manquant.");
                return ResponseEntity.status(400).body("Refresh token manquant.");
            }

            String username = jwtUtil.extractUsername(refreshToken);
            log.debug("Email extrait du refresh token : {}", username);

            UserDetails userDetails = jwtServiceInterface.loadUserByUsername(username);
            log.debug("Détails de l'utilisateur chargés : {}", userDetails);

            if (!jwtUtil.validateToken(refreshToken, userDetails.getUsername())) {
                log.warn("Échec du rafraîchissement du token : Refresh token invalide ou expiré.");
                return ResponseEntity.status(403).body("Refresh token invalide ou expiré.");
            }

            String newAccessToken = jwtUtil.generateAccessToken(userDetails.getUsername());
            log.debug("Nouveau Access Token généré : {}", newAccessToken);

            // Retourner les nouveaux tokens
            log.info("Rafraîchissement du token réussi pour l'utilisateur avec l'email : {}", username);
            return ResponseEntity.ok(new Token(newAccessToken, refreshToken));

        } catch (ExpiredJwtException e) {
            log.warn("Le refresh token a expiré.");
            return ResponseEntity.status(403).body("Le refresh token a expiré.");
        } catch (JwtException e) {
            log.warn("Le refresh token est invalide. Message : {}", e.getMessage());
            return ResponseEntity.status(403).body("Le refresh token est invalide.");
        } catch (Exception e) {
            log.error("Erreur lors du rafraîchissement du token : {}", e.getMessage());
            return ResponseEntity.status(500).body("Erreur interne du serveur.");
        }
    }
}
