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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EtudiantServiceImpl implements EtudiantService {

    private final EtudiantRepository etudiantRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public EtudiantResponseDTO createEtudiant(EtudiantDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }

        // 1. Création du compte utilisateur associé
        AppUser user = new AppUser();
        user.setFullname(dto.getFullname());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.ETUDIANT);
        AppUser savedUser = userRepository.save(user);

        // 2. Création de l'étudiant
        Etudiant etudiant = new Etudiant();
        etudiant.setMatricule(dto.getMatricule());
        etudiant.setUser(savedUser);
        Etudiant savedEtudiant = etudiantRepository.save(etudiant);

        return mapToResponseDTO(savedEtudiant);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EtudiantResponseDTO> getAllEtudiants(Long id, String matricule, String fullname, Pageable pageable) {
        return etudiantRepository.searchEtudiants(id, matricule, fullname, pageable)
                .map(this::mapToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public EtudiantResponseDTO getEtudiantById(Long id) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Étudiant introuvable avec l'ID : " + id));
        return mapToResponseDTO(etudiant);
    }

    @Override
    @Transactional
    public EtudiantResponseDTO updateEtudiant(Long id, EtudiantDTO dto) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Étudiant introuvable avec l'ID : " + id));

        AppUser user = etudiant.getUser();
        user.setFullname(dto.getFullname());
        user.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        userRepository.save(user);

        etudiant.setMatricule(dto.getMatricule());
        Etudiant updatedEtudiant = etudiantRepository.save(etudiant);

        return mapToResponseDTO(updatedEtudiant);
    }

    @Override
    @Transactional
    public void deleteEtudiant(Long id) {
        Etudiant etudiant = etudiantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Étudiant introuvable avec l'ID : " + id));
        etudiantRepository.delete(etudiant);
        userRepository.delete(etudiant.getUser());
    }

    /**
     * Méthode de mapping adaptée à la nature immuable du record EtudiantResponseDTO
     */
    private EtudiantResponseDTO mapToResponseDTO(Etudiant etudiant) {
        return new EtudiantResponseDTO(
                etudiant.getId(),
                etudiant.getMatricule(),
                etudiant.getUser().getFullname(),
                etudiant.getUser().getEmail(),
                etudiant.getPhotoUrl()
        );
    }
}