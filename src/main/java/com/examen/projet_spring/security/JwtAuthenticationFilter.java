package com.examen.projet_spring.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Diagnostic 1 : Entrée dans le filtre
        System.out.println("=== DIAGO : Nouvelle requête sur " + request.getRequestURI() + " ===");

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("=== DIAGO : Pas de header Authorization ou pas de Bearer ! ===");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        System.out.println("=== DIAGO : Jeton JWT extrait de la requête ===");

        try {
            userEmail = jwtService.extractUsername(jwt);
            System.out.println("=== DIAGO : Email extrait du Jeton : " + userEmail + " ===");

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtService.isTokenValid(jwt, userEmail)) {
                    String role = jwtService.extractRole(jwt);
                    System.out.println("=== DIAGO : Rôle extrait du Jeton : " + role + " ===");

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userEmail,
                            null,
                            List.of(new SimpleGrantedAuthority(role))
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("=== DIAGO : Authentification réussie et injectée dans le contexte ! ===");
                } else {
                    System.out.println("=== DIAGO : Le jeton JWT est invalide ou expiré ! ===");
                }
            }
        } catch (Exception e) {
            System.out.println("=== DIAGO ERREUR : Échec de la lecture ou validation du Jeton ! Raison : " + e.getMessage() + " ===");
        }

        filterChain.doFilter(request, response);
    }
}