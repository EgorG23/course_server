package com.hse.course.test;

import com.hse.course.model.User;
import com.hse.course.repository.UserRepository;
import com.hse.course.config.JwtService;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/test")
public class JwtTestController {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public JwtTestController(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @GetMapping("/token/{email}")
    public String generateToken(@PathVariable String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return jwtService.generateToken(user);
    }
}