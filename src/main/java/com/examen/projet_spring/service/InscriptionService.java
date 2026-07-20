package com.examen.projet_spring.service;

import com.examen.projet_spring.dto.InscriptionDTO;
import com.examen.projet_spring.dto.InscriptionResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InscriptionService {

    InscriptionResponseDTO inscrire(InscriptionDTO dto);

    Page<InscriptionResponseDTO> obtenirToutes(Pageable pageable);

    void supprimer(Long id);
}