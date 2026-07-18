package com.examen.projet_spring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtudiantDTO {

        @NotBlank(message = "Le matricule est obligatoire")
        private String matricule;

        @NotBlank(message = "Le nom est obligatoire")
        private String nom;

        @NotBlank(message = "Le prénom est obligatoire")
        private String prenom;

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Format de l'email invalide")
        private String email;

        private String filiere;

        @NotNull(message = "La date de naissance est obligatoire")
        @JsonProperty("dateNaissance")
        @JsonFormat(pattern = "yyyy-MM-dd") // Assure le format Année-Mois-Jour
        private LocalDate dateNaissance;

        @NotBlank(message = "Le lieu de naissance est obligatoire")
        @JsonProperty("lieuNaissance")
        private String lieuNaissance;
}