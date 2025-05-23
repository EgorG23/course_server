package com.hse.course.auth;

import com.hse.course.config.JwtService;
import com.hse.course.model.LoyaltyCard;
import com.hse.course.model.LoyaltyLevel;
import com.hse.course.model.User;
import com.hse.course.repository.LoyaltyCardRepository;
import com.hse.course.repository.UserRepository;
import com.hse.course.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import com.hse.course.dto.RegisterRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final LoyaltyCardRepository loyaltyCardRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }
        if (request.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
        }

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .userName(request.getUserName())
                .surname(request.getSurname())
                .phoneNumber(request.getPhoneNumber())
                .dateOfBirth(request.getDob())
                .build();

        userRepository.save(user);

        LoyaltyCard loyaltyCard = LoyaltyCard.builder()
                .userId(user.getId())
                .loyaltyLevel(LoyaltyLevel.valueOf(LoyaltyLevel.STANDARD.name()))
                .build();

        loyaltyCardRepository.save(loyaltyCard);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .message("Registration successful")
                .globalId(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getUserName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .dob(user.getDateOfBirth())
                .build();
    }


    public AuthResponse authenticate(AuthRequest request) throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return AuthResponse.builder()
                .token(jwtService.generateToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .globalId(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getUserName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .dob(user.getDateOfBirth())
                .build();
    }

    public AuthResponse refreshToken(String refreshToken) {
        String email = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return AuthResponse.builder()
                .token(newAccessToken)
                .refreshToken(newRefreshToken)
                .message("Tokens refreshed")
                .globalId(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getUserName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .dob(user.getDateOfBirth())
                .build();
    }
}