package com.example.demo.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping({"/", "/healthz"})
    public Map<String, String> healthz() {
        return Map.of(
                "service", "Hospital Management System API",
                "status", "running",
                "health", "/api/hospital/health",
                "statusEndpoint", "/api/hospital/status",
                "countsEndpoint", "/api/hospital/counts"
        );
    }
}