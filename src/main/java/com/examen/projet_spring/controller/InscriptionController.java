package com.examen.projet_spring.controller;

import com.examen.projet_spring.dto.InscriptionDTO;
import com.examen.projet_spring.dto.InscriptionResponseDTO;
import com.examen.projet_spring.service.InscriptionService;
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
@RequestMapping("/api/inscriptions")
@RequiredArgsConstructor
public class InscriptionController {

    private final InscriptionService inscriptionService;

    // Inscrire un étudiant à un cours (ADMIN seulement, cf. cahier des charges)
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<InscriptionResponseDTO> inscrire(@Valid @RequestBody InscriptionDTO dto) {
        return new ResponseEntity<>(inscriptionService.inscrire(dto), HttpStatus.CREATED);
    }

    // Liste paginée des inscriptions (ADMIN ou ENSEIGNANT)
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ENSEIGNANT')")
    public ResponseEntity<Page<InscriptionResponseDTO>> getAllInscriptions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(inscriptionService.obtenirToutes(pageable));
    }

    // Annuler/supprimer une inscription (ADMIN seulement)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        inscriptionService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}