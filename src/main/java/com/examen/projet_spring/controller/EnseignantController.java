package com.examen.projet_spring.controller;

import com.examen.projet_spring.dto.EnseignantDTO;
import com.examen.projet_spring.dto.EnseignantResponseDTO;
import com.examen.projet_spring.service.EnseignantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/enseignants")
@RequiredArgsConstructor
public class EnseignantController {

    private final EnseignantService enseignantService;


    @PostMapping
    public ResponseEntity<EnseignantResponseDTO> createEnseignant(
            @Valid @RequestBody EnseignantDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(enseignantService.createEnseignant(dto));
    }


    @GetMapping
    public ResponseEntity<Page<EnseignantResponseDTO>> getAllEnseignants(
            Pageable pageable) {

        return ResponseEntity.ok(
                enseignantService.getAllEnseignants(pageable)
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<EnseignantResponseDTO> getEnseignantById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                enseignantService.getEnseignantById(id)
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<EnseignantResponseDTO> updateEnseignant(
            @PathVariable Long id,
            @Valid @RequestBody EnseignantDTO dto) {

        return ResponseEntity.ok(
                enseignantService.updateEnseignant(id, dto)
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnseignant(
            @PathVariable Long id) {

        enseignantService.deleteEnseignant(id);

        return ResponseEntity.noContent().build();
    }
}