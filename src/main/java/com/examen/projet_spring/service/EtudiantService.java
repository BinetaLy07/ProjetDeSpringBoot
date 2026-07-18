package com.examen.projet_spring.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.examen.projet_spring.dto.EtudiantDTO;
import com.examen.projet_spring.dto.EtudiantResponseDTO;

public interface EtudiantService {
    EtudiantResponseDTO createEtudiant(EtudiantDTO dto);
    EtudiantResponseDTO getEtudiantById(Long id);
    EtudiantResponseDTO getEtudiantByMatricule(String matricule);
    EtudiantResponseDTO getEtudiantByEmail(String email);
    Page<EtudiantResponseDTO> getAllEtudiants(Long id, String matricule, String fullname, Pageable pageable);
    EtudiantResponseDTO updateEtudiant(Long id, EtudiantDTO dto);
    void deleteEtudiant(Long id);
    EtudiantResponseDTO restaurerEtudiant(Long id);
}