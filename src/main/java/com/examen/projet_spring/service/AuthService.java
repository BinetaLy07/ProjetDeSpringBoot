package com.examen.projet_spring.service;

import com.examen.projet_spring.security.AuthResponse;
import com.examen.projet_spring.security.LoginRequest;
import com.examen.projet_spring.security.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}