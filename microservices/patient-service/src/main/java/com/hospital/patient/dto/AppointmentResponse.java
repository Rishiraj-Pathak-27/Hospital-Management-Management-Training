package com.hospital.patient.dto;

public record AppointmentResponse(
        Integer appointmentId,
        Integer patientId,
        String patientName,
        Integer age,
        String disease,
        Integer doctorId,
        String doctorName,
        String specialization,
        String appointmentDate
) {
}
