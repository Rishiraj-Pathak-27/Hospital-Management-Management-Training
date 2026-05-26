package com.hospital.patient.controller;

import com.hospital.patient.dto.AdmissionRequest;
import com.hospital.patient.dto.AdmissionResponse;
import com.hospital.patient.dto.AppointmentRequest;
import com.hospital.patient.dto.AppointmentResponse;
import com.hospital.patient.dto.BedResponse;
import com.hospital.patient.dto.DoctorRequest;
import com.hospital.patient.dto.DoctorResponse;
import com.hospital.patient.dto.PatientRequest;
import com.hospital.patient.dto.PatientResponse;
import com.hospital.patient.dto.WardResponse;
import com.hospital.patient.service.HospitalManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@CrossOrigin("*")
public class HospitalController {

    private final HospitalManagementService hospitalManagementService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "patient-service");
        response.put("status", "UP");
        response.put("port", 8086);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "patient-service");
        response.put("status", "UP");
        response.put("message", "Patient service is running");
        response.put("port", 8086);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> root() {
        return info();
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.put("username", authentication.getName());

        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        response.put("roles", roles);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/patients")
    public ResponseEntity<PatientResponse> addPatient(@Valid @RequestBody PatientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hospitalManagementService.addPatient(request));
    }

    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponse>> viewPatients() {
        return ResponseEntity.ok(hospitalManagementService.viewPatients());
    }

    @PostMapping("/doctors")
    public ResponseEntity<DoctorResponse> addDoctor(@Valid @RequestBody DoctorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hospitalManagementService.addDoctor(request));
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponse>> viewDoctors() {
        return ResponseEntity.ok(hospitalManagementService.viewDoctors());
    }

    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponse> bookAppointment(@Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hospitalManagementService.bookAppointment(request));
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponse>> viewAppointments() {
        return ResponseEntity.ok(hospitalManagementService.viewAppointments());
    }

    @PostMapping("/admissions")
    public ResponseEntity<AdmissionResponse> admitPatient(@Valid @RequestBody AdmissionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hospitalManagementService.admitPatient(request));
    }

    @GetMapping("/wards")
    public ResponseEntity<List<WardResponse>> viewWards() {
        return ResponseEntity.ok(hospitalManagementService.viewWards());
    }

    @GetMapping("/beds")
    public ResponseEntity<List<BedResponse>> viewBeds() {
        return ResponseEntity.ok(hospitalManagementService.viewBeds());
    }

    @GetMapping("/admissions")
    public ResponseEntity<List<AdmissionResponse>> viewAdmissions() {
        return ResponseEntity.ok(hospitalManagementService.viewAdmissions());
    }

}
