package com.hse.course.auth;

import com.hse.course.config.JwtService;
import com.hse.course.dto.AuthRequest;
import com.hse.course.dto.RegisterRequest;
import com.hse.course.repository.UserRepository;
import com.hse.course.service.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private JwtService jwtService;
    private UserRepository userRepository;

    @PostMapping("/user/registration")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/user/login")
    public ResponseEntity<ApiResponse> authenticate(@RequestBody AuthRequest request) throws Exception {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Refresh token is missing");
        }
        String refreshToken = authHeader.substring(7);
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }
}
