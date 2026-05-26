package com.hospital.patient.dto;

public record BedResponse(
        Integer bedId,
        String bedNumber,
        Boolean available,
        Integer wardId,
        String wardName,
        String wardType
) {
}
