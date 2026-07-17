package com.examen.projet_spring.security;

public record AuthResponse(
        String accessToken,
        String tokenType
) {}