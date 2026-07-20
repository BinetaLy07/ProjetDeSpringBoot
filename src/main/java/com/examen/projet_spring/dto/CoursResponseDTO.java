package com.examen.projet_spring.dto;

public record CoursResponseDTO(
        Long id,
        String titre,
        String description,
        Long enseignantId,
        String enseignantNom
) {}