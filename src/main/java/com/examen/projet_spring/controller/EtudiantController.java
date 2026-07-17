package com.examen.projet_spring.controller;

import com.examen.projet_spring.dto.EtudiantDTO;
import com.examen.projet_spring.dto.EtudiantResponseDTO;
import com.examen.projet_spring.service.EtudiantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/etudiants")
@RequiredArgsConstructor
public class EtudiantController {

    private final EtudiantService etudiantService;

    // Créer un étudiant (ADMIN seulement)
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<EtudiantResponseDTO> createEtudiant(@Valid @RequestBody EtudiantDTO dto) {
        return new ResponseEntity<>(etudiantService.createEtudiant(dto), HttpStatus.CREATED);
    }

    // Récupérer la liste paginée et filtrée (ADMIN ou ENSEIGNANT)
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ENSEIGNANT')")
    public ResponseEntity<Page<EtudiantResponseDTO>> getAllEtudiants(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String matricule,
            @RequestParam(required = false) String fullname,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(etudiantService.getAllEtudiants(id, matricule, fullname, pageable));
    }

    // Récupérer un étudiant par ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ENSEIGNANT')")
    public ResponseEntity<EtudiantResponseDTO> getEtudiantById(@PathVariable Long id) {
        return ResponseEntity.ok(etudiantService.getEtudiantById(id));
    }

    // Modifier un étudiant (ADMIN seulement)
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<EtudiantResponseDTO> updateEtudiant(
            @PathVariable Long id,
            @Valid @RequestBody EtudiantDTO dto
    ) {
        return ResponseEntity.ok(etudiantService.updateEtudiant(id, dto));
    }

    // Supprimer un étudiant (ADMIN seulement)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteEtudiant(@PathVariable Long id) {
        etudiantService.deleteEtudiant(id);
        return ResponseEntity.noContent().build();
    }
}