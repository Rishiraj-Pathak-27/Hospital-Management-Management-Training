package com.example.demo.service;

import com.example.demo.dto.HospitalCountsResponse;
import com.example.demo.repository.HospitalRepository;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;
import com.example.demo.dto.WardDto;
import com.example.demo.dto.BedDto;
import com.example.demo.dto.AdmissionDto;
import org.springframework.transaction.annotation.Transactional;

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

    public List<WardDto> getWards() {
        return hospitalRepository.fetchWards();
    }

    public List<BedDto> getBeds() {
        return hospitalRepository.fetchBeds();
    }

    public List<AdmissionDto> getAdmissions() {
        return hospitalRepository.fetchAdmissions();
    }

    @Transactional
    public AdmissionDto processAdmission(int patientId, String severity, String notes) {
        if (!hospitalRepository.patientExists(patientId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found: " + patientId);
        }

        String normalized = severity == null ? "" : severity.trim().toLowerCase();

        boolean consultOnly = normalized.contains("low") || normalized.contains("mild") || normalized.contains("minor");

        Integer wardId = null;
        Integer bedId = null;
        String status = consultOnly ? "CONSULT_ONLY" : "ADMITTED";

        if (!consultOnly) {
            String wardType = normalized.contains("critical") ? "ICU" : (normalized.contains("high") || normalized.contains("severe") ? "Emergency" : "General");
            var opt = hospitalRepository.findAvailableBedByWardType(wardType);
            if (opt.isPresent()) {
                BedDto b = opt.get();
                wardId = b.wardId();
                bedId = b.bedId();
                hospitalRepository.markBedUnavailable(b.bedId());
            } else {
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No available bed for severity: " + severity);
            }
        }

        String date = LocalDate.now().toString();
        hospitalRepository.insertAdmission(patientId, severity, status, wardId, bedId, date, notes);

        return new AdmissionDto(-1, patientId, severity, status, date, wardId, bedId, null, null, notes);
    }
}