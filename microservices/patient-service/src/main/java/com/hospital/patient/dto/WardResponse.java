package com.hospital.patient.dto;

public record WardResponse(Integer wardId, String wardName, String wardType, Integer severityRank, Integer totalBeds) {
}
