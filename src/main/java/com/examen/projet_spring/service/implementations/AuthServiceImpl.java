package com.examen.projet_spring.service.implementations;

import com.examen.projet_spring.domain.AppUser;
import com.examen.projet_spring.repository.UserRepository;
import com.examen.projet_spring.security.AuthResponse;
import com.examen.projet_spring.security.JwtService;
import com.examen.projet_spring.security.LoginRequest;
import com.examen.projet_spring.security.RegisterRequest;
import com.examen.projet_spring.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Cet email est déjà enregistré !");
        }

        AppUser user = AppUser.builder()
                .fullname(request.fullname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password())) // Hachage BCrypt
                .role(request.role())
                .build();

        userRepository.save(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        AppUser user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé !"));

        String token = jwtService.generateAccessToken(user);
        return new AuthResponse(token, "Bearer");
    }
}