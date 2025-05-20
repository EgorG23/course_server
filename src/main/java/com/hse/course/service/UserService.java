package com.hse.course.service;

import com.hse.course.dto.RegisterRequest;
import com.hse.course.model.User;
import com.hse.course.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public ApiResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return new ApiResponse("Email already exists", false);
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserName(request.getUserName());

        User savedUser = userRepository.save(user);
        return new ApiResponse(savedUser, true);
    }

    public ApiResponse getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(u -> new ApiResponse(u, true))
                .orElse(new ApiResponse("User not found", false));
    }

    public ApiResponse getUserById(String id) {
        try {
            Long userId = Long.parseLong(id);
            Optional<User> user = userRepository.findById(userId);
            return user.map(u -> new ApiResponse(u, true))
                    .orElse(new ApiResponse("User not found", false));
        } catch (NumberFormatException e) {
            return new ApiResponse("Invalid ID format", false);
        }
    }

    @Transactional
    public ApiResponse updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            return new ApiResponse("User not found", false);
        }
        User updatedUser = userRepository.save(user);
        return new ApiResponse(updatedUser, true);
    }

    @Transactional
    public ApiResponse uploadAvatar(String id, byte[] avatarData) {
        try {
            Long userId = Long.parseLong(id);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Path uploadDir = Paths.get("uploads");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String fileName = "avatar_" + userId + ".jpg";
            Path filePath = uploadDir.resolve(fileName);
            Files.write(filePath, avatarData);

            user.setAvatarPath(fileName);
            userRepository.save(user);

            return new ApiResponse("Avatar uploaded", true);
        } catch (Exception e) {
            return new ApiResponse("Upload failed: " + e.getMessage(), false);
        }
    }

    public ResponseEntity<byte[]> getAvatar(String id) {
        try {
            Long userId = Long.parseLong(id);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Path filePath = Paths.get("uploads/" + user.getAvatarPath());
            byte[] imageBytes = Files.readAllBytes(filePath);

            return ResponseEntity.ok()
                    .header("Content-Type", "image/jpeg")
                    .body(imageBytes);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @Transactional
    public ApiResponse updateInterests(Long userId, String interests) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setInterest(interests); // Или используйте JSON
        userRepository.save(user);
        return new ApiResponse("Interests updated", true);
    }

    public ApiResponse getUserInterests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new ApiResponse(user.getInterest(), true); // Возвращаем Set<Integer>
    }
}