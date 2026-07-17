package com.examen.projet_spring.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CoursDTO(
        @NotBlank(message = "Le titre du cours est obligatoire")
        String titre,

        String description,

        @NotNull(message = "L'ID de l'enseignant est obligatoire")
        Long enseignantId
) {}