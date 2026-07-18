package com.examen.projet_spring.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "etudiants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String matricule;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(unique = true, nullable = false)
    private String email;

    private String filiere;

    @Column(name = "date_naissance")
    @JsonProperty("dateNaissance")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateNaissance;

    @Column(name = "lieu_naissance")
    @JsonProperty("lieuNaissance")
    private String lieuNaissance;

    @Builder.Default
    @Column(nullable = false)
    private boolean deleted = false;
}