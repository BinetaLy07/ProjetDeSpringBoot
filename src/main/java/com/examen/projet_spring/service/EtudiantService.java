package com.examen.projet_spring.service;

import com.examen.projet_spring.dto.EtudiantDTO;
import com.examen.projet_spring.dto.EtudiantResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EtudiantService {
    EtudiantResponseDTO createEtudiant(EtudiantDTO dto);

    // Modification ici : ajout des paramètres de recherche
    Page<EtudiantResponseDTO> getAllEtudiants(Long id, String matricule, String fullname, Pageable pageable);

    EtudiantResponseDTO getEtudiantById(Long id);
    EtudiantResponseDTO updateEtudiant(Long id, EtudiantDTO dto);
    void deleteEtudiant(Long id);
}