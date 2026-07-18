package com.examen.projet_spring.dto;

import java.time.LocalDate;

public record InscriptionResponseDTO(
        Long id,
        Long etudiantId,
        String etudiantNom,
        Long coursId,
        String coursTitre,
        LocalDate dateInscription
) {}