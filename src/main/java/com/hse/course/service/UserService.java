package com.hse.course.service;

import com.google.gson.Gson;
import com.hse.course.dto.RegisterRequest;
import com.hse.course.dto.UserResponse;
import com.hse.course.model.LoyaltyCard;
import com.hse.course.model.LoyaltyLevel;
import com.hse.course.model.User;
import com.hse.course.repository.LoyaltyCardRepository;
import com.hse.course.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.hse.course.dto.RegisterRequest;
import com.hse.course.dto.UserResponse;
import com.hse.course.model.LoyaltyCard;
import com.hse.course.model.User;
import com.hse.course.repository.LoyaltyCardRepository;
import com.hse.course.repository.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoyaltyCardRepository loyaltyCardRepository;
    private final Gson gson=new Gson();

    public UserService(UserRepository userRepository, LoyaltyCardRepository loyaltyCardRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loyaltyCardRepository = loyaltyCardRepository;
    }

    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .surname(request.getSurname())
                .phoneNumber(request.getPhoneNumber())
                .dateOfBirth(request.getDob())
                .build();

        return userRepository.save(user);
    }

    public ApiResponse getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(u -> new ApiResponse(u, true))
                .orElse(new ApiResponse("User not found", false));
    }

    public ApiResponse getUserById(String id) {
        try {
            Long userId = Long.parseLong(id);
            Optional<User> userOpt = userRepository.findById(userId);

            if (userOpt.isEmpty()) {
                return new ApiResponse("User is not found", false);
            }

            User user = userOpt.get();
            UserResponse response = UserResponse.builder()
                    .globalId(user.getId())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .name(user.getName())
                    .surname(user.getSurname())
                    .phoneNumber(user.getPhoneNumber())
                    .dob(user.getDateOfBirth())
                    .build();

            return new ApiResponse(response, true);
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
    // На потом
//    @Transactional
//    public ApiResponse uploadAvatar(String id, byte[] avatarData) {
//        try {
//            Long userId = Long.parseLong(id);
//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            Path uploadDir = Paths.get("uploads");
//            if (!Files.exists(uploadDir)) {
//                Files.createDirectories(uploadDir);
//            }
//
//            String fileName = "avatar_" + userId + ".jpg";
//            Path filePath = uploadDir.resolve(fileName);
//            Files.write(filePath, avatarData);
//
//            user.setAvatarPath(fileName);
//            userRepository.save(user);
//
//            return new ApiResponse("Avatar uploaded", true);
//        } catch (Exception e) {
//            return new ApiResponse("Upload failed: " + e.getMessage(), false);
//        }
//    }
//
//    public ResponseEntity<byte[]> getAvatar(String id) {
//        try {
//            Long userId = Long.parseLong(id);
//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new RuntimeException("User not found"));
//
//            Path filePath = Paths.get("uploads/" + user.getAvatarPath());
//            byte[] imageBytes = Files.readAllBytes(filePath);
//
//            return ResponseEntity.ok()
//                    .header("Content-Type", "image/jpeg")
//                    .body(imageBytes);
//        } catch (Exception e) {
//            return ResponseEntity.notFound().build();
//        }
//    }
    @Transactional
    public ApiResponse updateInterests(Long userId, String interests) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User is not found"));

        user.setInterest(interests);
        userRepository.save(user);
        return new ApiResponse("Interests updated", true);
    }

    public ApiResponse getUserInterests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User is not found"));

        return new ApiResponse(user.getInterest(), true);
    }

    public ApiResponse getLoyaltyCard(String userGlobalId) {
        try {
            Long userId = Long.valueOf(userGlobalId);
            LoyaltyCard card = loyaltyCardRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("Loyalty card is not found"));

            return new ApiResponse(card, true);
        } catch (NumberFormatException e) {
            return new ApiResponse("Invalid user ID format", false);
        }
    }

    public ResponseEntity<byte[]> getPhoto(String path) { //Не забудь поменять путь!
        try {
            File photo = new File("\"C:\\Users\\huawei\\Desktop\"" + path);
            if (!photo.exists()) {
                photo = new File("04.png");
                System.out.println("def");
            }

            byte[] imageBytes = Files.readAllBytes(Path.of(photo.getAbsolutePath()));

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
