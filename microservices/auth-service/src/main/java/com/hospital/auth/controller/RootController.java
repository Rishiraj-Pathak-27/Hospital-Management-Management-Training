package com.hospital.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/api/auth/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "auth-service");
        response.put("status", "UP");
        response.put("message", "Auth service is running");
        response.put("port", 8085);
        return ResponseEntity.ok(response);
    }

}
