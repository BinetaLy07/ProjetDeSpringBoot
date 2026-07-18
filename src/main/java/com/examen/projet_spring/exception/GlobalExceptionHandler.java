package com.examen.projet_spring.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Gère précisément la distinction entre introuvable et supprimé
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> body = new LinkedHashMap<>(); // LinkedHashMap conserve l'ordre d'affichage

        body.put("erreur", "Ressource introuvable");

        // Extraction du message dynamique passé par le service
        if (ex.getMessage() != null && ex.getMessage().startsWith("DELETED_OR_NOT_FOUND:")) {
            String messagePrecis = ex.getMessage().replace("DELETED_OR_NOT_FOUND:", "");
            body.put("cause_postman", messagePrecis);
        } else {
            body.put("cause_postman", ex.getMessage());
        }

        body.put("date_erreur", LocalDateTime.now().toString());
        body.put("statut", HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // 2. Gère les erreurs de typage dans l'URL (/api/etudiants/texte)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, Object> body = new LinkedHashMap<>();

        body.put("erreur", "Format de paramètre invalide");

        String nomParametre = ex.getName();
        String valeurSaisie = ex.getValue() != null ? ex.getValue().toString() : "";

        body.put("cause_postman", "L'URL attend un identifiant numérique (Long) pour le paramètre '" + nomParametre +
                "', mais vous avez écrit \"" + valeurSaisie + "\". Pour rechercher par nom ou email, utilisez les endpoints dédiés.");

        body.put("date_erreur", LocalDateTime.now().toString());
        body.put("statut", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}