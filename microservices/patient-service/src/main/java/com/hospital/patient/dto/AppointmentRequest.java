package com.hospital.patient.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AppointmentRequest(
        @NotNull(message = "Appointment Id is required") Integer appointmentId,
        @NotNull(message = "Patient Id is required") Integer patientId,
        @NotNull(message = "Doctor Id is required") Integer doctorId,
        @NotBlank(message = "Appointment date is required") String appointmentDate
) {
}
