package com.example.demo.dto;

public record HospitalCountsResponse(long patients, long doctors, long appointments, long totalRecords) {
}