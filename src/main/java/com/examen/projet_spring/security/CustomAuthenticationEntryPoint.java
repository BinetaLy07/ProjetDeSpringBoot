package com.examen.projet_spring.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Récupération du message d'erreur précis mis par le JwtAuthenticationFilter
        String messagePrecision = "Accès refusé. Authentification requise.";
        final Object jwtError = request.getAttribute("jwt_error");

        if (jwtError != null) {
            messagePrecision = jwtError.toString();
        } else if (authException != null && !"Full authentication is required to access this resource".equals(authException.getMessage())) {
            messagePrecision = authException.getMessage();
        }

        // Utilisation de LinkedHashMap pour conserver un ordre d'affichage propre dans Postman
        final Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", messagePrecision); // Affiche maintenant le problème exact
        body.put("path", request.getServletPath());
        body.put("timestamp", LocalDateTime.now().toString());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}