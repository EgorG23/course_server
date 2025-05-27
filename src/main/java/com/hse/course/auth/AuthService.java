package com.hse.course.auth;

import com.google.gson.Gson;
import com.hse.course.config.JwtService;
import com.hse.course.model.LoyaltyCard;
import com.hse.course.model.LoyaltyLevel;
import com.hse.course.model.User;
import com.hse.course.repository.LoyaltyCardRepository;
import com.hse.course.repository.UserRepository;
import com.hse.course.dto.*;
import com.hse.course.service.ApiResponse;
import lombok.RequiredArgsConstructor;
import com.hse.course.dto.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final LoyaltyCardRepository loyaltyCardRepository;
    private final Gson gson;

    public ApiResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Данная почта уже используется");
        }
        if (request.getPassword().length() < 6) {
            throw new IllegalArgumentException("Пароль должен быть из не менее чем 6 символлв");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
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
        var userResponse = UserResponse.builder()
                .globalId(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .dob(user.getDateOfBirth())
                .build();

        return new ApiResponse(gson.toJson(userResponse), true);

    }

    public ApiResponse authenticate(AuthRequest request) throws Exception {
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User is not found"));
        var userResponse = UserResponse.builder()
                .globalId(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .name(user.getName())
                .surname(user.getSurname())
                .phoneNumber(user.getPhoneNumber())
                .dob(user.getDateOfBirth())
                .build();

        return new ApiResponse(gson.toJson(userResponse), true);

    }



    public ApiResponse refreshToken(String refreshToken) {
        String email = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User is not found"));
        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new RuntimeException("Invalid refresh token");
        }
        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        return new ApiResponse(gson.toJson(user), true);

    }
}