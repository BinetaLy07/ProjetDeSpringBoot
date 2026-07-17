package com.examen.projet_spring.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data // <-- Cette annotation génère automatiquement getEmail(), getPassword(), etc.
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantDTO {
        private String matricule;
        private String fullname;
        private String email;     // <-- Vérifie que ce champ est bien écrit en minuscules
        private String password;
}