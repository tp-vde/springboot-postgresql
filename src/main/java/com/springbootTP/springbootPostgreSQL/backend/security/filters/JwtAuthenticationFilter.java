package com.springbootTP.springbootPostgreSQL.backend.security.filters;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Récupérer l'en-tête "Authorization" de la requête
        String authHeader = request.getHeader("Authorization");

        // Vérifier si l'en-tête est présent et commence par "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Si non, passer la requête au filtre suivant
            filterChain.doFilter(request, response);
            return;
        }

        // Extraire le token JWT (en supprimant "Bearer ")
        String token = authHeader.substring(7);
        String username = null;

        try {
            // Extraire le nom d'utilisateur (email) du token
            username = jwtUtil.getUsernameFromToken(token);
        } catch (ExpiredJwtException e) {
            System.out.println("Le token a expiré : " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Token mal formé : " + e.getMessage());
        } catch (SignatureException e) {
            System.out.println("Signature du token invalide : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Le token est vide ou null : " + e.getMessage());
        }

        // Si le nom d'utilisateur est valide et qu'il n'y a pas d'authentification en cours
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Charger les détails de l'utilisateur à partir de la base de données
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Valider le token JWT
            if (jwtUtil.validateToken(token, userDetails)) {
                // Créer un objet d'authentification
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                // Ajouter les détails de la requête (comme l'adresse IP) à l'authentification
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Définir l'authentification dans le contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Passer la requête au filtre suivant
        filterChain.doFilter(request, response);
    }
}