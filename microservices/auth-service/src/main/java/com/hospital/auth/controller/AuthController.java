package com.hospital.auth.controller;

import com.hospital.auth.dto.AuthResponse;
import com.hospital.auth.dto.LoginRequest;
import com.hospital.auth.dto.RegisterRequest;
import com.hospital.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/register")
    public ResponseEntity<Map<String, Object>> registerHelp() {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Method not allowed. Use POST /api/auth/register");
        response.put("example", Map.of(
                "username", "john",
                "password", "pass123",
                "role", "PATIENT"
        ));
        response.put("status", 405);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> loginHelp() {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Method not allowed. Use POST /api/auth/login");
        response.put("example", Map.of(
                "username", "john",
                "password", "pass123"
        ));
        response.put("status", 405);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @GetMapping("/validate")
    public ResponseEntity<AuthResponse> validate(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        String token = extractTokenFromHeader(authHeader);

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(AuthResponse.validateFailure("Missing or invalid Authorization header"));
        }

        AuthResponse response = authService.validate(token);

        if (!response.getValid()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "auth-service");
        response.put("status", "UP");
        response.put("port", 8085);
        return ResponseEntity.ok(response);
    }

    private String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

}
