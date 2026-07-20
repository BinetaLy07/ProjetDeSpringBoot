package com.examen.projet_spring.service;

import com.examen.projet_spring.exception.InvalidFileTypeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.UUID;
import com.examen.projet_spring.domain.UploadedFile;
import com.examen.projet_spring.exception.InvalidFileTypeException;

@Service
public class FileStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private static final long MAX_PHOTO_SIZE = 2 * 1024 * 1024; // 2 Mo

    /** Upload photo profil : accepte uniquement JPEG et PNG, vérifiés par signature binaire. */
    public UploadedFile storePhoto(Long etudiantId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileTypeException("Le fichier envoyé est vide.");
        }
        if (file.getSize() > MAX_PHOTO_SIZE) {
            throw new InvalidFileTypeException("La photo dépasse la taille maximale autorisée (2 Mo).");
        }

        String detectedType = detectRealMimeType(file);
        if (!detectedType.equals("image/jpeg") && !detectedType.equals("image/png")) {
            throw new InvalidFileTypeException("Format de photo invalide. Seuls JPEG et PNG sont acceptés.");
        }

        return saveToDisk(etudiantId, file, UploadedFile.FileType.PHOTO, detectedType, "photos");
    }

    /** Upload document : accepte uniquement les vrais PDF, vérifiés par signature binaire. */
    public UploadedFile storeDocument(Long etudiantId, MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileTypeException("Le fichier envoyé est vide.");
        }

        String detectedType = detectRealMimeType(file);
        if (!detectedType.equals("application/pdf")) {
            throw new InvalidFileTypeException("Format de document invalide. Seul le PDF est accepté.");
        }

        return saveToDisk(etudiantId, file, UploadedFile.FileType.DOCUMENT, detectedType, "docs");
    }

    /** Lit les premiers octets du fichier pour déterminer son vrai type, indépendamment de son extension. */
    private String detectRealMimeType(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            byte[] header = new byte[8];
            int read = is.read(header);
            if (read < 4) {
                throw new InvalidFileTypeException("Fichier illisible ou corrompu.");
            }

            if ((header[0] & 0xFF) == 0xFF && (header[1] & 0xFF) == 0xD8 && (header[2] & 0xFF) == 0xFF) {
                return "image/jpeg";
            }
            if ((header[0] & 0xFF) == 0x89 && header[1] == 'P' && header[2] == 'N' && header[3] == 'G') {
                return "image/png";
            }
            if (header[0] == '%' && header[1] == 'P' && header[2] == 'D' && header[3] == 'F') {
                return "application/pdf";
            }
            return "application/octet-stream";
        } catch (IOException e) {
            throw new InvalidFileTypeException("Erreur lors de la lecture du fichier : " + e.getMessage());
        }
    }

    private UploadedFile saveToDisk(Long etudiantId, MultipartFile file, UploadedFile.FileType type,
                                    String contentType, String subDir) {
        try {
            Path targetDir = Paths.get(uploadDir, subDir);
            Files.createDirectories(targetDir);

            String extension = contentType.equals("application/pdf") ? ".pdf"
                    : contentType.equals("image/png") ? ".png" : ".jpg";
            String storedName = UUID.randomUUID() + extension;
            Path targetPath = targetDir.resolve(storedName);

            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            UploadedFile entity = new UploadedFile();
            entity.setEtudiantId(etudiantId);
            entity.setType(type);
            entity.setFileName(file.getOriginalFilename());
            entity.setFilePath(targetPath.toString());
            entity.setContentType(contentType);
            entity.setSize(file.getSize());
            entity.setUploadedAt(LocalDateTime.now());
            return entity;
        } catch (IOException e) {
            throw new RuntimeException("Impossible d'enregistrer le fichier : " + e.getMessage());
        }
    }
}