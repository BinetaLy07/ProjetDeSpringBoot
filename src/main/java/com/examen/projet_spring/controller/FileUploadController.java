package com.examen.projet_spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.examen.projet_spring.domain.UploadedFile;
import com.examen.projet_spring.repository.UploadedFileRepository;
import com.examen.projet_spring.service.FileStorageService;

@RestController
@RequestMapping("/etudiants")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileStorageService fileStorageService;
    private final UploadedFileRepository uploadedFileRepository;

    @PostMapping(value = "/{id}/photo", consumes = "multipart/form-data")
    public ResponseEntity<UploadedFile> uploadPhoto(@PathVariable Long id,
                                                    @RequestParam("file") MultipartFile file) {
        UploadedFile saved = fileStorageService.storePhoto(id, file);
        uploadedFileRepository.save(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping(value = "/{id}/docs", consumes = "multipart/form-data")
    public ResponseEntity<UploadedFile> uploadDocument(@PathVariable Long id,
                                                       @RequestParam("file") MultipartFile file) {
        UploadedFile saved = fileStorageService.storeDocument(id, file);
        uploadedFileRepository.save(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}