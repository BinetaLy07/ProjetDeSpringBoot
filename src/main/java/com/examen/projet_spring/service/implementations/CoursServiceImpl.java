package com.examen.projet_spring.service.implementations;

import com.examen.projet_spring.domain.Cours;
import com.examen.projet_spring.domain.Enseignant;
import com.examen.projet_spring.dto.CoursDTO;
import com.examen.projet_spring.dto.CoursResponseDTO;
import com.examen.projet_spring.exception.ResourceNotFoundException;
import com.examen.projet_spring.repository.CoursRepository;
import com.examen.projet_spring.repository.EnseignantRepository;
import com.examen.projet_spring.service.CoursService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CoursServiceImpl implements CoursService {

    private final CoursRepository coursRepository;
    private final EnseignantRepository enseignantRepository;

    @Override
    @Transactional
    public CoursResponseDTO creer(CoursDTO dto) {
        Enseignant enseignant = enseignantRepository.findById(dto.enseignantId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enseignant introuvable avec l'id : " + dto.enseignantId()));

        Cours cours = Cours.builder()
                .titre(dto.titre())
                .description(dto.description())
                .enseignant(enseignant)
                .build();

        return toResponseDTO(coursRepository.save(cours));
    }

    @Override
    @Transactional
    public CoursResponseDTO modifier(Long id, CoursDTO dto) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cours introuvable avec l'id : " + id));

        Enseignant enseignant = enseignantRepository.findById(dto.enseignantId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Enseignant introuvable avec l'id : " + dto.enseignantId()));

        cours.setTitre(dto.titre());
        cours.setDescription(dto.description());
        cours.setEnseignant(enseignant);

        return toResponseDTO(coursRepository.save(cours));
    }

    @Override
    public CoursResponseDTO obtenirParId(Long id) {
        Cours cours = coursRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cours introuvable avec l'id : " + id));
        return toResponseDTO(cours);
    }

    @Override
    public Page<CoursResponseDTO> obtenirTous(Pageable pageable) {
        return coursRepository.findAll(pageable).map(this::toResponseDTO);
    }

    @Override
    @Transactional
    public void supprimer(Long id) {
        if (!coursRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cours introuvable avec l'id : " + id);
        }
        coursRepository.deleteById(id);
    }

    private CoursResponseDTO toResponseDTO(Cours cours) {
        return new CoursResponseDTO(
                cours.getId(),
                cours.getTitre(),
                cours.getDescription(),
                cours.getEnseignant() != null ? cours.getEnseignant().getId() : null,
                cours.getEnseignant() != null && cours.getEnseignant().getUser() != null
                        ? cours.getEnseignant().getUser().getFullname() : null
        );
    }
}