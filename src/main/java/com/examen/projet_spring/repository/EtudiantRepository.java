package com.examen.projet_spring.repository;

import com.examen.projet_spring.model.Etudiant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // AJOUTER
import org.springframework.data.repository.query.Param; // AJOUTER
import org.springframework.stereotype.Repository;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Page<Etudiant> findByDeletedFalse(Pageable pageable);
    boolean existsByEmail(String email);
    boolean existsByMatricule(String matricule);

    // AJOUTER CETTE MÉTHODE
    @Query("SELECT e FROM Etudiant e WHERE e.deleted = false " +
            "AND (:matricule IS NULL OR e.matricule = :matricule) " +
            "AND (:fullname IS NULL OR CONCAT(e.prenom, ' ', e.nom) LIKE %:fullname% OR CONCAT(e.nom, ' ', e.prenom) LIKE %:fullname%)")
    Page<Etudiant> searchEtudiants(@Param("matricule") String matricule,
                                   @Param("fullname") String fullname,
                                   Pageable pageable);
}