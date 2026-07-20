package com.examen.projet_spring.service.implementations;

import com.examen.projet_spring.domain.Cours;
import com.examen.projet_spring.domain.Etudiant;
import com.examen.projet_spring.domain.Inscription;
import com.examen.projet_spring.dto.InscriptionDTO;
import com.examen.projet_spring.dto.InscriptionResponseDTO;
import com.examen.projet_spring.exception.ResourceNotFoundException;
import com.examen.projet_spring.repository.CoursRepository;
import com.examen.projet_spring.repository.EtudiantRepository;
import com.examen.projet_spring.repository.InscriptionRepository;
import com.examen.projet_spring.service.InscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class InscriptionServiceImpl implements InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final EtudiantRepository etudiantRepository;
    private final CoursRepository coursRepository;

    @Override
    @Transactional
    public InscriptionResponseDTO inscrire(InscriptionDTO dto) {
        Etudiant etudiant = etudiantRepository.findById(dto.etudiantId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Étudiant introuvable avec l'id : " + dto.etudiantId()));

        Cours cours = coursRepository.findById(dto.coursId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cours introuvable avec l'id : " + dto.coursId()));

        Inscription inscription = Inscription.builder()
                .etudiant(etudiant)
                .cours(cours)
                .dateInscription(LocalDate.now())
                .build();

        return toResponseDTO(inscriptionRepository.save(inscription));
    }

    @Override
    public Page<InscriptionResponseDTO> obtenirToutes(Pageable pageable) {
        return inscriptionRepository.findAll(pageable).map(this::toResponseDTO);
    }

    @Override
    @Transactional
    public void supprimer(Long id) {
        if (!inscriptionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inscription introuvable avec l'id : " + id);
        }
        inscriptionRepository.deleteById(id);
    }

    private InscriptionResponseDTO toResponseDTO(Inscription inscription) {
        return new InscriptionResponseDTO(
                inscription.getId(),
                inscription.getEtudiant().getId(),
                inscription.getEtudiant().getUser() != null
                        ? inscription.getEtudiant().getUser().getFullname() : null,
                inscription.getCours().getId(),
                inscription.getCours().getTitre(),
                inscription.getDateInscription()
        );
    }
}