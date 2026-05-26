package com.hospital.patient.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DoctorRequest(
        @NotNull(message = "Doctor Id is required") Integer doctorId,
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Specialization is required") String specialization
) {
}
