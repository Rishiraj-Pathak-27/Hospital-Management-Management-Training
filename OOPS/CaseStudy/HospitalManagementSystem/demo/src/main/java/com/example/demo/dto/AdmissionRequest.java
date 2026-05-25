package com.example.demo.dto;

public class AdmissionRequest {
    public int patientId;
    public String severity;
    public String notes;

    public AdmissionRequest() {}

    public AdmissionRequest(int patientId, String severity, String notes) {
        this.patientId = patientId;
        this.severity = severity;
        this.notes = notes;
    }
}
