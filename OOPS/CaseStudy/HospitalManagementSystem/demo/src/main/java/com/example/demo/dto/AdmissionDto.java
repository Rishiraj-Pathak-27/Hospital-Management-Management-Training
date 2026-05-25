package com.example.demo.dto;

public record AdmissionDto(int admissionId, int patientId, String severity, String admissionStatus, String admissionDate, Integer wardId, Integer bedId, String wardName, String bedNumber, String notes) {
}
