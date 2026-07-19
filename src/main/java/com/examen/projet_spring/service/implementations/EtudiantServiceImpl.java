package com.examen.projet_spring.service.implementations;

import com.examen.projet_spring.dto.EtudiantDTO;
import com.examen.projet_spring.dto.EtudiantResponseDTO;
import com.examen.projet_spring.service.EtudiantService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EtudiantServiceImpl implements EtudiantService {


    @Override
    public EtudiantResponseDTO createEtudiant(EtudiantDTO dto) {
        return null;
    }


    @Override
    public Page<EtudiantResponseDTO> getAllEtudiants(
            Long id,
            String matricule,
            String fullname,
            Pageable pageable
    ) {
        return null;
    }


    @Override
    public EtudiantResponseDTO getEtudiantById(Long id) {
        return null;
    }


    @Override
    public EtudiantResponseDTO updateEtudiant(Long id, EtudiantDTO dto) {
        return null;
    }


    @Override
    public void deleteEtudiant(Long id) {

    }
}