package com.examen.projet_spring.dto;

import jakarta.validation.constraints.NotNull;

public record InscriptionDTO(

        Long id,

        @NotNull(message = "L'ID de l'étudiant est obligatoire")
        Long etudiantId,

        @NotNull(message = "L'ID du cours est obligatoire")
        Long coursId
) {}