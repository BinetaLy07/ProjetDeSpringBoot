package com.examen.projet_spring.dto;

public record EtudiantResponseDTO(
        Long id,
        String matricule,
        String fullname,
        String email,
        String photoUrl
) {}