package com.examen.projet_spring.security;

import com.examen.projet_spring.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
        @NotBlank(message = "Le nom complet est obligatoire")
        String fullname,

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format d'email invalide")
        String email,

        @NotBlank(message = "Le mot de passe est obligatoire")
        String password,

        @NotNull(message = "Le rôle est obligatoire")
        Role role
) {}