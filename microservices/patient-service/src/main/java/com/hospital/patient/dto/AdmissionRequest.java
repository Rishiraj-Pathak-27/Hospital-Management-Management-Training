package com.hospital.patient.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdmissionRequest(
        @NotNull(message = "Patient Id is required") Integer patientId,
        @NotBlank(message = "Severity is required") String severity,
        @NotBlank(message = "Notes are required") String notes
) {
}
