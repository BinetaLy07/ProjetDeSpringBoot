package com.examen.projet_spring.service.implementations;

import com.examen.projet_spring.domain.AppUser;
import com.examen.projet_spring.domain.Etudiant;
import com.examen.projet_spring.domain.Role;
import com.examen.projet_spring.dto.EtudiantDTO;
import com.examen.projet_spring.dto.EtudiantResponseDTO;
import com.examen.projet_spring.repository.EtudiantRepository;
import com.examen.projet_spring.repository.UserRepository;
import com.examen.projet_spring.service.EtudiantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EtudiantServiceImpl implements EtudiantService {

    private final EtudiantRepository etudiantRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public EtudiantResponseDTO createEtudiant(EtudiantDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé !");
        }

        AppUser user = AppUser.builder()
                .fullname(dto.getFullname())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.ETUDIANT)
                .build();

        userRepository.save(user);


        Etudiant etudiant = Etudiant.builder()
                .matricule(dto.getMatricule())
                .user(user)
                .build();

        Etudiant saved = etudiantRepository.save(etudiant);

        return new EtudiantResponseDTO(
                saved.getId(),
                saved.getMatricule(),
                saved.getUser().getFullname(),
                saved.getUser().getEmail(),
                saved.getPhotoUrl()
        );
    }


    @Override
    public Page<EtudiantResponseDTO> getAllEtudiants(
            Long id,
            String matricule,
            String fullname,
            Pageable pageable
    ) {
        return etudiantRepository.findAll(pageable)
                .map(e -> new EtudiantResponseDTO(
                        e.getId(),
                        e.getMatricule(),
                        e.getUser().getFullname(),
                        e.getUser().getEmail(),
                        e.getPhotoUrl()
                ));
    }


    @Override
    public EtudiantResponseDTO getEtudiantById(Long id) {

        Etudiant e = etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etudiant introuvable"));

        return new EtudiantResponseDTO(
                e.getId(),
                e.getMatricule(),
                e.getUser().getFullname(),
                e.getUser().getEmail(),
                e.getPhotoUrl()
        );
    }


    @Override
    public EtudiantResponseDTO updateEtudiant(Long id, EtudiantDTO dto) {
        return null;
    }


    @Override
    public void deleteEtudiant(Long id) {

        Etudiant e = etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Etudiant introuvable"));

        etudiantRepository.delete(e);
    }
}