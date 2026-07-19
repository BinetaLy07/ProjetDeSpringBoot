package com.examen.projet_spring.controller;

import com.examen.projet_spring.dto.CoursDTO;
import com.examen.projet_spring.dto.CoursResponseDTO;
import com.examen.projet_spring.service.CoursService;
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
@RequestMapping("/api/cours")
@RequiredArgsConstructor
public class CoursController {

    private final CoursService coursService;

    // Créer un cours (ADMIN seulement)
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CoursResponseDTO> creerCours(@Valid @RequestBody CoursDTO dto) {
        return new ResponseEntity<>(coursService.creer(dto), HttpStatus.CREATED);
    }

    // Liste paginée (tous les utilisateurs authentifiés)
    @GetMapping
    public ResponseEntity<Page<CoursResponseDTO>> getAllCours(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(coursService.obtenirTous(pageable));
    }

    // Récupérer un cours par ID
    @GetMapping("/{id}")
    public ResponseEntity<CoursResponseDTO> getCoursById(@PathVariable Long id) {
        return ResponseEntity.ok(coursService.obtenirParId(id));
    }

    // Modifier un cours (ADMIN seulement)
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CoursResponseDTO> updateCours(
            @PathVariable Long id,
            @Valid @RequestBody CoursDTO dto
    ) {
        return ResponseEntity.ok(coursService.modifier(id, dto));
    }

    // Supprimer un cours (ADMIN seulement)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCours(@PathVariable Long id) {
        coursService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}