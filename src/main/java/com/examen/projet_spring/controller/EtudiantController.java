package com.examen.projet_spring.controller;

import com.examen.projet_spring.dto.EtudiantDTO;
import com.examen.projet_spring.dto.EtudiantResponseDTO;
import com.examen.projet_spring.service.EtudiantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/etudiants")
@RequiredArgsConstructor
public class EtudiantController {

    private final EtudiantService etudiantService;


    @PostMapping
    public ResponseEntity<EtudiantResponseDTO> createEtudiant(
            @Valid @RequestBody EtudiantDTO dto
    ) {
        return new ResponseEntity<>(
                etudiantService.createEtudiant(dto),
                HttpStatus.CREATED
        );
    }


    @GetMapping
    public ResponseEntity<Page<EtudiantResponseDTO>> getAllEtudiants(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String matricule,
            @RequestParam(required = false) String fullname,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                etudiantService.getAllEtudiants(
                        id,
                        matricule,
                        fullname,
                        pageable
                )
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<EtudiantResponseDTO> getEtudiantById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                etudiantService.getEtudiantById(id)
        );
    }


    @PutMapping("/{id}")
    public ResponseEntity<EtudiantResponseDTO> updateEtudiant(
            @PathVariable Long id,
            @Valid @RequestBody EtudiantDTO dto
    ) {
        return ResponseEntity.ok(
                etudiantService.updateEtudiant(id, dto)
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEtudiant(
            @PathVariable Long id
    ) {
        etudiantService.deleteEtudiant(id);
        return ResponseEntity.ok("Etudiant supprimé avec succès !");
    }
}