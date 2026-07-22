package com.examen.projet_spring.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private String filiere;

    @Column(name = "date_naissance")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateNaissance;

    @Column(name = "lieu_naissance")
    private String lieuNaissance;

    private String photoUrl;

    @Builder.Default
    private boolean deleted = false;


    @OneToOne
    @JoinColumn(name = "user_id")
    private AppUser user;
}