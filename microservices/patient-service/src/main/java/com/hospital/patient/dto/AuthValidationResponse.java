package com.hospital.patient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthValidationResponse(Boolean valid, String username, String role, String message) {
}
