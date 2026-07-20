package com.examen.projet_spring.service;

import com.examen.projet_spring.dto.EnseignantDTO;
import com.examen.projet_spring.dto.EnseignantResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EnseignantService {

    EnseignantResponseDTO createEnseignant(EnseignantDTO dto);

    Page<EnseignantResponseDTO> getAllEnseignants(Pageable pageable);

    EnseignantResponseDTO getEnseignantById(Long id);

    EnseignantResponseDTO updateEnseignant(Long id, EnseignantDTO dto);

    void deleteEnseignant(Long id);
}