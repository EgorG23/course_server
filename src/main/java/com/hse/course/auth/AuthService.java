package com.hse.course.auth;

import com.hse.course.config.JwtService;
import com.hse.course.model.User;
import com.hse.course.repository.UserRepository;
import com.hse.course.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import com.hse.course.dto.RegisterRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private AuthenticationManager authManager;

    public AuthResponse register(RegisterRequest request) {  // ← Исправлено
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email уже занят");
        }

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .userName(request.getUserName())
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .message("Регистрация успешна")
                .userId(user.getId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authManager.authenticate(  // ← Исправлено
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        String jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .message("Вход выполнен")
                .userId(user.getId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .build();
    }
}