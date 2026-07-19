package com.examen.projet_spring.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
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

        System.out.println("=== DIAGO : Nouvelle requête sur " + request.getRequestURI() + " ===");

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("=== DIAGO : Pas de header Authorization ou pas de Bearer ! ===");
            request.setAttribute("jwt_error", "Jeton d'authentification absent (Format 'Bearer <token>' requis).");
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
                    request.setAttribute("jwt_error", "Le jeton d'authentification est invalide ou expiré.");
                }
            }
        } catch (ExpiredJwtException e) {
            System.out.println("=== DIAGO ERREUR : Le jeton a expiré ! ===");
            request.setAttribute("jwt_error", "Le jeton d'authentification a expiré.");
        } catch (MalformedJwtException e) {
            System.out.println("=== DIAGO ERREUR : Le jeton est mal formé ! ===");
            request.setAttribute("jwt_error", "Le jeton d'authentification est mal formé ou corrompu.");
        } catch (SignatureException e) {
            System.out.println("=== DIAGO ERREUR : Signature du jeton invalide ! ===");
            request.setAttribute("jwt_error", "La signature du jeton d'authentification est invalide.");
        } catch (Exception e) {
            System.out.println("=== DIAGO ERREUR : Échec de validation ! Raison : " + e.getMessage() + " ===");
            request.setAttribute("jwt_error", "Erreur lors de la validation du jeton : " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}