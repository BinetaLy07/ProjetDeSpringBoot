package com.examen.projet_spring.service;

import com.examen.projet_spring.dto.CoursDTO;
import com.examen.projet_spring.dto.CoursResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CoursService {

    CoursResponseDTO creer(CoursDTO dto);

    CoursResponseDTO modifier(Long id, CoursDTO dto);

    CoursResponseDTO obtenirParId(Long id);

    Page<CoursResponseDTO> obtenirTous(Pageable pageable);

    void supprimer(Long id);
}