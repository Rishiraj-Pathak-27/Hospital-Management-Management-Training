package com.hospital.patient.dto;

public record AdmissionResponse(
        Integer admissionId,
        String admissionDate,
        String severity,
        String admissionStatus,
        Integer patientId,
        String patientName,
        Integer age,
        String disease,
        String wardName,
        String wardType,
        String bedNumber,
        String notes
) {
}
