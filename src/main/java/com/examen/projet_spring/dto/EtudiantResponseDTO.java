package com.examen.projet_spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantResponseDTO {
    private Long id;
    private String matricule;
    private String nom;
    private String prenom;
    private String email;
    private String filiere;
    private LocalDate dateNaissance; // Ajouté
    private String lieuNaissance; // Ajouté
}