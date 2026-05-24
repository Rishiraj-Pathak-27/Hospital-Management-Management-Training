package com.example.demo.controller;

import com.example.demo.dto.HospitalCountsResponse;
import com.example.demo.dto.HospitalStatusResponse;
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

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
                "service", "Hospital Management System API",
                "status", "running"
        );
    }

    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", "Hospital Management System API is running");
    }

    @GetMapping("/status")
    public HospitalStatusResponse status() {
        HospitalCountsResponse counts = hospitalService.getAllCounts();
        return new HospitalStatusResponse(
                "Hospital Management System API",
                "running",
                "/api/hospital/health",
                "/api/hospital/counts",
                counts.patients(),
                counts.doctors(),
                counts.appointments(),
                counts.totalRecords()
        );
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