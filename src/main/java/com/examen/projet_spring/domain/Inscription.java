package com.examen.projet_spring.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

// Importation de la classe Etudiant depuis le package model
import com.examen.projet_spring.model.Etudiant;

// Si ta classe Cours est aussi dans le package model, décommente la ligne suivante :
// import com.examen.projet_spring.model.Cours;

@Entity
@Table(name = "inscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateInscription;

    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    @ManyToOne
    @JoinColumn(name = "cours_id", nullable = false)
    private Cours cours;
}