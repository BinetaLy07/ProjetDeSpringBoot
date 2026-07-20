package com.examen.projet_spring.repository;

import com.examen.projet_spring.domain.Etudiant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    Optional<Etudiant> findByMatricule(String matricule);

    // Recherche multi-critères paginée
    @Query("SELECT e FROM Etudiant e JOIN e.user u WHERE " +
            "(:id IS NULL OR e.id = :id) AND " +
            "(:matricule IS NULL OR UPPER(e.matricule) LIKE UPPER(CONCAT('%', :matricule, '%'))) AND " +
            "(:fullname IS NULL OR UPPER(u.fullname) LIKE UPPER(CONCAT('%', :fullname, '%')))")
    Page<Etudiant> searchEtudiants(
            @Param("id") Long id,
            @Param("matricule") String matricule,
            @Param("fullname") String fullname,
            Pageable pageable
    );
}