package com.springbootTP.springbootPostgreSQL.backend.security.controller;


import com.springbootTP.springbootPostgreSQL.backend.security.filters.JwtUtil;
import com.springbootTP.springbootPostgreSQL.backend.security.model.Token;
import com.springbootTP.springbootPostgreSQL.backend.security.model.Users;
import com.springbootTP.springbootPostgreSQL.backend.security.service.JwtService;
import com.springbootTP.springbootPostgreSQL.backend.security.service.JwtServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtServiceInterface jwtServiceInterface;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // Injection via constructeur (meilleure pratique que @Autowired sur les champs)
    public AuthController(JwtService JwtService, JwtServiceInterface jwtServiceInterface, JwtUtil jwtUtil,
                          AuthenticationManager authenticationManager ) {

        this.jwtServiceInterface = jwtServiceInterface;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;

    }

    /**
     * Inscription d'un nouvel utilisateur
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Users users) {
        jwtServiceInterface.registerUser(users.getEmail(), users.getUsername(), users.getPassword(), users.getRole());
        return ResponseEntity.ok("Utilisateur enregistré avec succès !");
    }

    /**
     * Authentification et génération de tokens
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users users) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(users.getEmail(), users.getPassword())
            );

            UserDetails userDetails = jwtServiceInterface.loadUserByUsername(users.getEmail());
            String accessToken = jwtUtil.generateAccessToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            System.out.println("Utlisateur trouvé " + users);

            return ResponseEntity.ok(new Token(accessToken, refreshToken));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Email ou mot de passe incorrect !");
        }
    }

    /**
     * Rafraîchir le token d'accès avec le refresh token
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Token token) {
        try {
            String refreshToken = token.getRefreshToken();
            String username = jwtUtil.getUsernameFromToken(refreshToken);
            UserDetails userDetails = jwtServiceInterface.loadUserByUsername(username);

            if (!jwtUtil.validateToken(refreshToken, userDetails)) {
                return ResponseEntity.status(403).body("Refresh token invalide ou expiré.");
            }

            String newAccessToken = jwtUtil.generateAccessToken(userDetails);
            return ResponseEntity.ok(new Token(newAccessToken, refreshToken));

        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erreur lors du rafraîchissement du token.");
        }
    }
}
