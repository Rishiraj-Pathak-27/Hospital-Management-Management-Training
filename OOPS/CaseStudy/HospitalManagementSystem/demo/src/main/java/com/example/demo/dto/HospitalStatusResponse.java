package com.example.demo.dto;

public record HospitalStatusResponse(
        String service,
        String status,
        String healthEndpoint,
        String countsEndpoint,
        long patients,
        long doctors,
        long appointments,
        long totalRecords
) {
}