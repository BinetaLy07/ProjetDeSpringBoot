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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/etudiants")
@RequiredArgsConstructor
public class EtudiantController {

    private final EtudiantService etudiantService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> createEtudiant(@Valid @RequestBody EtudiantDTO dto) {
        EtudiantResponseDTO cree = etudiantService.createEtudiant(dto);
        Map<String, Object> response = new HashMap<>();
        response.put("date_operation", LocalDateTime.now().toString());
        response.put("message", "L'étudiant a été créé avec succès !");
        response.put("etudiant", cree);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> createPlusieursEtudiants(@Valid @RequestBody List<EtudiantDTO> dtos) {
        List<EtudiantResponseDTO> crees = etudiantService.createEtudiants(dtos);
        Map<String, Object> response = new HashMap<>();
        response.put("date_operation", LocalDateTime.now().toString());
        response.put("message", crees.size() + " étudiants ont été créés avec succès !");
        response.put("etudiants", crees);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updatePlusieursEtudiants(@Valid @RequestBody List<EtudiantDTO> dtos) {
        List<Map<String, Object>> detailsModifications = new ArrayList<>();

        for (EtudiantDTO dto : dtos) {
            // 1. Récupérer l'état actuel pour comparer
            EtudiantResponseDTO actuel = etudiantService.getEtudiantById(dto.getId());

            // 2. Faire la mise à jour
            etudiantService.updateEtudiant(dto.getId(), dto);

            // 3. Détecter les modifications
            Map<String, Object> mods = detecterModifications(actuel, dto);

            // 4. On n'ajoute à la liste QUE si des modifications ont été détectées
            if (!mods.isEmpty()) {
                Map<String, Object> detail = new LinkedHashMap<>();
                detail.put("id", dto.getId());
                detail.put("message", genererMessageModif(actuel, mods));
                detail.put("champsModifies", mods);
                detailsModifications.add(detail);
            }
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("date_operation", LocalDateTime.now().toString());

        if (detailsModifications.isEmpty()) {
            response.put("message", "Aucune modification n'a été effectuée sur les étudiants fournis.");
        } else {
            response.put("message", detailsModifications.size() + " étudiant(s) ont été mis à jour.");
            response.put("resultats", detailsModifications);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ENSEIGNANT')")
    public ResponseEntity<Page<EtudiantResponseDTO>> listerTousLesEtudiants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EtudiantResponseDTO> resultat = etudiantService.getAllEtudiants(null, null, null, pageable);
        return ResponseEntity.ok(resultat);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENSEIGNANT')")
    public ResponseEntity<EtudiantResponseDTO> obtenirParId(@PathVariable Long id) {
        return ResponseEntity.ok(etudiantService.getEtudiantById(id));
    }

    @GetMapping("/matricule/{matricule}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENSEIGNANT')")
    public ResponseEntity<EtudiantResponseDTO> obtenirParMatricule(@PathVariable String matricule) {
        return ResponseEntity.ok(etudiantService.getEtudiantByMatricule(matricule));
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENSEIGNANT')")
    public ResponseEntity<EtudiantResponseDTO> obtenirParEmail(@PathVariable String email) {
        return ResponseEntity.ok(etudiantService.getEtudiantByEmail(email));
    }

    @GetMapping("/recherche")
    @PreAuthorize("hasAnyRole('ADMIN', 'ENSEIGNANT')")
    public ResponseEntity<?> rechercheGlobale(
            @RequestParam(required = false) String matricule,
            @RequestParam(required = false) String fullname,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EtudiantResponseDTO> resultat = etudiantService.getAllEtudiants(null, matricule, fullname, pageable);

        if (resultat.isEmpty()) {
            Map<String, String> erreur = new HashMap<>();
            erreur.put("message", "Aucun étudiant trouvé pour les critères : "
                    + (matricule != null ? "Matricule=" + matricule + " " : "")
                    + (fullname != null ? "Nom=" + fullname : ""));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erreur);
        }
        return ResponseEntity.ok(resultat);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateEtudiant(
            @PathVariable Long id,
            @Valid @RequestBody EtudiantDTO dto
    ) {
        EtudiantResponseDTO actuel = etudiantService.getEtudiantById(id);
        etudiantService.updateEtudiant(id, dto);

        Map<String, Object> response = new LinkedHashMap<>();
        Map<String, Object> modifications = detecterModifications(actuel, dto);
        response.put("date_operation", LocalDateTime.now().toString());

        if (!modifications.isEmpty()) {
            String message = genererMessageModif(actuel, modifications);
            response.put("message", message);
            response.put("champsModifies", modifications);
        } else {
            response.put("message", "Aucune modification détectée.");
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> deleteEtudiant(@PathVariable Long id) {
        EtudiantResponseDTO etudiant = etudiantService.getEtudiantById(id);
        etudiantService.deleteEtudiant(id);
        Map<String, Object> response = new HashMap<>();
        response.put("date_operation", LocalDateTime.now().toString());
        response.put("message", "L'étudiant " + etudiant.getPrenom() + " " + etudiant.getNom() + " a été supprimé avec succès !");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/restaurer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> restaurerEtudiant(@PathVariable Long id) {
        EtudiantResponseDTO restaure = etudiantService.restaurerEtudiant(id);
        Map<String, Object> response = new HashMap<>();
        response.put("date_operation", LocalDateTime.now().toString());
        response.put("message", "L'étudiant a été restauré avec succès !");
        response.put("etudiant", restaure);
        return ResponseEntity.ok(response);
    }

    private Map<String, Object> detecterModifications(EtudiantResponseDTO actuel, EtudiantDTO dto) {
        Map<String, Object> mods = new LinkedHashMap<>();
        if (dto.getNom() != null && !dto.getNom().equals(actuel.getNom())) mods.put("nom", dto.getNom());
        if (dto.getPrenom() != null && !dto.getPrenom().equals(actuel.getPrenom())) mods.put("prenom", dto.getPrenom());
        if (dto.getEmail() != null && !dto.getEmail().equals(actuel.getEmail())) mods.put("email", dto.getEmail());
        if (dto.getFiliere() != null && !dto.getFiliere().equals(actuel.getFiliere())) mods.put("filiere", dto.getFiliere());
        if (dto.getMatricule() != null && !dto.getMatricule().equals(actuel.getMatricule())) mods.put("matricule", dto.getMatricule());
        if (dto.getDateNaissance() != null && !dto.getDateNaissance().equals(actuel.getDateNaissance())) mods.put("dateNaissance", dto.getDateNaissance().toString());
        if (dto.getLieuNaissance() != null && !dto.getLieuNaissance().equals(actuel.getLieuNaissance())) mods.put("lieuNaissance", dto.getLieuNaissance());
        return mods;
    }

    private String genererMessageModif(EtudiantResponseDTO actuel, Map<String, Object> mods) {
        String identite = actuel.getPrenom() + " " + actuel.getNom();
        if (mods.size() == 1) {
            if (mods.containsKey("dateNaissance")) return "La date de naissance de " + identite + " a été modifiée avec succès !";
            if (mods.containsKey("filiere")) return "La filière de " + identite + " a été modifiée avec succès !";
            if (mods.containsKey("lieuNaissance")) return "Le lieu de naissance de " + identite + " a été modifié avec succès !";
            if (mods.containsKey("email")) return "L'adresse email de " + identite + " a été modifiée avec succès !";
            if (mods.containsKey("matricule")) return "Le matricule de " + identite + " a été modifié avec succès !";
        }
        return "L'étudiant " + identite + " a été modifié avec succès !";
    }
}