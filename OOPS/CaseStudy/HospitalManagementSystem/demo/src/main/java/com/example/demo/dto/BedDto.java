package com.example.demo.dto;

public record BedDto(int bedId, int wardId, String bedNumber, boolean isAvailable, String wardName, String wardType) {
}
