package com.example.demo.controller;

import com.example.demo.dto.HospitalCountsResponse;
import com.example.demo.service.HospitalService;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hospital")
public class HospitalController {

    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @GetMapping({"", "/"})
    public Map<String, String> home() {
        return Map.of(
                "service", "Hospital Management System API",
                "status", "running",
                "healthEndpoint", "/api/hospital/hello",
                "countsEndpoint", "/api/hospital/counts"
        );
    }

    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", "Hospital Management System API is running");
    }

    @GetMapping("/counts")
    public HospitalCountsResponse counts() {
        return hospitalService.getAllCounts();
    }

    @GetMapping("/counts/{entity}")
    public Map<String, Long> countByEntity(@PathVariable String entity) {
        return Map.of(entity, hospitalService.getCountForEntity(entity));
    }
}