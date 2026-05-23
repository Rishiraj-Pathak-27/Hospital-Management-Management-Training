package com.example.demo.service;

import com.example.demo.dto.HospitalCountsResponse;
import com.example.demo.repository.HospitalRepository;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    public HospitalCountsResponse getAllCounts() {
        Map<String, Long> counts = hospitalRepository.fetchAllCounts();
        return new HospitalCountsResponse(
                counts.getOrDefault("patients", 0L),
                counts.getOrDefault("doctors", 0L),
                counts.getOrDefault("appointments", 0L),
                counts.getOrDefault("totalRecords", 0L)
        );
    }

    public long getCountForEntity(String entity) {
        return switch (entity.toLowerCase()) {
            case "patients", "patient" -> hospitalRepository.countPatients();
            case "doctors", "doctor" -> hospitalRepository.countDoctors();
            case "appointments", "appointment" -> hospitalRepository.countAppointments();
            default -> throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Unknown entity: " + entity + ". Use patients, doctors, or appointments."
            );
        };
    }
}