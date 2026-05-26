package com.hospital.patient.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PatientRequest(
        @NotNull(message = "Patient Id is required") Integer patientId,
        @NotBlank(message = "Name is required") String name,
        @NotNull(message = "Age is required") @Min(value = 0, message = "Age must be positive") Integer age,
        @NotBlank(message = "Disease is required") String disease
) {
}
