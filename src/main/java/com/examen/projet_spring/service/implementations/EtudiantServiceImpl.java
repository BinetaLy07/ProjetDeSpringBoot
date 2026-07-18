package com.examen.projet_spring.service.implementations;

import com.examen.projet_spring.dto.EtudiantDTO;
import com.examen.projet_spring.dto.EtudiantResponseDTO;
import com.examen.projet_spring.model.Etudiant;
import com.examen.projet_spring.repository.EtudiantRepository;
import com.examen.projet_spring.service.EtudiantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EtudiantServiceImpl implements EtudiantService {

    private final EtudiantRepository etudiantRepository;

    @Override
    @Transactional
    public EtudiantResponseDTO createEtudiant(EtudiantDTO dto) {
        // Vérification des doublons avant création
        if (etudiantRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("DELETED_OR_NOT_FOUND:Un étudiant avec l'email " + dto.getEmail() + " existe déjà.");
        }

        Etudiant etudiant = new Etudiant();
        etudiant.setNom(dto.getNom());
        etudiant.setPrenom(dto.getPrenom());
        etudiant.setEmail(dto.getEmail());
        etudiant.setFiliere(dto.getFiliere());
        etudiant.setDateNaissance(dto.getDateNaissance());
        etudiant.setLieuNaissance(dto.getLieuNaissance());
        etudiant.setDeleted(false);

        if (dto.getMatricule() == null || dto.getMatricule().isEmpty()) {
            etudiant.setMatricule("ETU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        } else {
            etudiant.setMatricule(dto.getMatricule());
        }
        return mapperEnResponseDTO(etudiantRepository.save(etudiant));
    }

    @Override
    public EtudiantResponseDTO getEtudiantById(Long id) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DELETED_OR_NOT_FOUND:L'étudiant avec l'ID " + id + " n'existe pas."));

        if (etudiant.isDeleted()) {
            throw new RuntimeException("DELETED_OR_NOT_FOUND:L'étudiant avec l'ID " + id + " a été supprimé.");
        }
        return mapperEnResponseDTO(etudiant);
    }

    @Override
    public EtudiantResponseDTO getEtudiantByMatricule(String matricule) {
        return etudiantRepository.findAll().stream()
                .filter(e -> e.getMatricule().equals(matricule) && !e.isDeleted())
                .findFirst()
                .map(this::mapperEnResponseDTO)
                .orElseThrow(() -> new RuntimeException("DELETED_OR_NOT_FOUND:Étudiant introuvable"));
    }

    @Override
    public EtudiantResponseDTO getEtudiantByEmail(String email) {
        return etudiantRepository.findAll().stream()
                .filter(e -> e.getEmail().equals(email) && !e.isDeleted())
                .findFirst()
                .map(this::mapperEnResponseDTO)
                .orElseThrow(() -> new RuntimeException("DELETED_OR_NOT_FOUND:Étudiant introuvable"));
    }

    @Override
    public Page<EtudiantResponseDTO> getAllEtudiants(Long id, String matricule, String fullname, Pageable pageable) {
        return etudiantRepository.findByDeletedFalse(pageable).map(this::mapperEnResponseDTO);
    }

    @Override
    @Transactional
    public EtudiantResponseDTO updateEtudiant(Long id, EtudiantDTO dto) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .filter(e -> !e.isDeleted())
                .orElseThrow(() -> new RuntimeException("DELETED_OR_NOT_FOUND:Étudiant introuvable ou supprimé"));

        if (dto.getNom() != null) etudiant.setNom(dto.getNom());
        if (dto.getPrenom() != null) etudiant.setPrenom(dto.getPrenom());
        if (dto.getEmail() != null) etudiant.setEmail(dto.getEmail());
        if (dto.getFiliere() != null) etudiant.setFiliere(dto.getFiliere());
        if (dto.getDateNaissance() != null) etudiant.setDateNaissance(dto.getDateNaissance());
        if (dto.getLieuNaissance() != null) etudiant.setLieuNaissance(dto.getLieuNaissance());

        return mapperEnResponseDTO(etudiantRepository.save(etudiant));
    }

    @Override
    @Transactional
    public void deleteEtudiant(Long id) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DELETED_OR_NOT_FOUND:Étudiant introuvable"));
        etudiant.setDeleted(true);
        etudiantRepository.save(etudiant);
    }

    @Override
    @Transactional
    public EtudiantResponseDTO restaurerEtudiant(Long id) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DELETED_OR_NOT_FOUND:Étudiant introuvable"));
        etudiant.setDeleted(false);
        return mapperEnResponseDTO(etudiantRepository.save(etudiant));
    }

    private EtudiantResponseDTO mapperEnResponseDTO(Etudiant etudiant) {
        EtudiantResponseDTO response = new EtudiantResponseDTO();
        response.setId(etudiant.getId());
        response.setNom(etudiant.getNom());
        response.setPrenom(etudiant.getPrenom());
        response.setEmail(etudiant.getEmail());
        response.setFiliere(etudiant.getFiliere());
        response.setMatricule(etudiant.getMatricule());
        response.setDateNaissance(etudiant.getDateNaissance());
        response.setLieuNaissance(etudiant.getLieuNaissance());
        return response;
    }
}