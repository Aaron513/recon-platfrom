package com.reconplatform.review_service.controller;

import com.reconplatform.review_service.model.AppUserEntity;
import com.reconplatform.review_service.repository.AppUserRepository;
import com.reconplatform.review_service.service.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(AppUserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<AppUserEntity> user = userRepository.findByUsername(request.username());
        if (user.isEmpty() || !passwordEncoder.matches(request.password(), user.get().getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }
        String token = jwtService.generateToken(user.get().getUsername(), user.get().getRole());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    public record LoginRequest(String username, String password) {}
    public record LoginResponse(String token) {}
}