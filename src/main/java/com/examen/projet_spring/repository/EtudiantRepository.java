package com.examen.projet_spring.repository;

import com.examen.projet_spring.model.Etudiant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Page<Etudiant> findByDeletedFalse(Pageable pageable);
    boolean existsByEmail(String email);
    boolean existsByMatricule(String matricule);
}