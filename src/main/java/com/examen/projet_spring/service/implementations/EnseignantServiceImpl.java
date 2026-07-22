package com.examen.projet_spring.service.implementations;

import com.examen.projet_spring.domain.AppUser;
import com.examen.projet_spring.domain.Enseignant;
import com.examen.projet_spring.domain.Role;
import com.examen.projet_spring.dto.EnseignantDTO;
import com.examen.projet_spring.dto.EnseignantResponseDTO;
import com.examen.projet_spring.repository.EnseignantRepository;
import com.examen.projet_spring.repository.UserRepository;
import com.examen.projet_spring.service.EnseignantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnseignantServiceImpl implements EnseignantService {

    private final EnseignantRepository enseignantRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public EnseignantResponseDTO createEnseignant(EnseignantDTO dto) {

        if (userRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("Cet email est déjà utilisé");
        }

        // Création du compte utilisateur
        AppUser user = new AppUser();

        user.setFullname(dto.fullname());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(Role.ENSEIGNANT);

        AppUser savedUser = userRepository.save(user);


        // Création de l'enseignant
        Enseignant enseignant = new Enseignant();

        enseignant.setSpecialite(dto.specialite());
        enseignant.setUser(savedUser);

        Enseignant savedEnseignant = enseignantRepository.save(enseignant);


        return mapToResponseDTO(savedEnseignant);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<EnseignantResponseDTO> getAllEnseignants(Pageable pageable) {

        return enseignantRepository.findAll(pageable)
                .map(this::mapToResponseDTO);
    }


    @Override
    @Transactional(readOnly = true)
    public EnseignantResponseDTO getEnseignantById(Long id) {

        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Enseignant introuvable"));

        return mapToResponseDTO(enseignant);
    }


    @Override
    @Transactional
    public EnseignantResponseDTO updateEnseignant(Long id, EnseignantDTO dto) {

        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Enseignant introuvable"));


        AppUser user = enseignant.getUser();

        user.setFullname(dto.fullname());
        user.setEmail(dto.email());

        if(dto.password() != null && !dto.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        userRepository.save(user);


        enseignant.setSpecialite(dto.specialite());

        Enseignant updated = enseignantRepository.save(enseignant);


        return mapToResponseDTO(updated);
    }


    @Override
    @Transactional
    public void deleteEnseignant(Long id) {

        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Enseignant introuvable"));


        enseignantRepository.delete(enseignant);

        userRepository.delete(enseignant.getUser());
    }



    private EnseignantResponseDTO mapToResponseDTO(Enseignant enseignant) {

        AppUser user = enseignant.getUser();

        return new EnseignantResponseDTO(
                enseignant.getId(),
                enseignant.getSpecialite(),
                user != null ? user.getFullname() : null,
                user != null ? user.getEmail() : null
        );
    }
}