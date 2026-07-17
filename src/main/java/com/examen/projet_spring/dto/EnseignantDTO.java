package com.examen.projet_spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EnseignantDTO(
        @NotBlank(message = "La spécialité est obligatoire")
        String specialite,

        @NotBlank(message = "Le nom complet est obligatoire")
        String fullname,

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format de l'email invalide")
        String email,

        @NotBlank(message = "Le mot de passe est obligatoire")
        String password
) {}