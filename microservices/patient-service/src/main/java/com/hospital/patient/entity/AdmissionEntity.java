package com.hospital.patient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "admissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdmissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admissionId")
    private Integer admissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patientId", nullable = false)
    private PatientEntity patient;

    @Column(nullable = false)
    private String severity;

    @Column(name = "admissionStatus", nullable = false)
    private String admissionStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wardId")
    private WardEntity ward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bedId")
    private BedEntity bed;

    @Column(name = "admissionDate", nullable = false)
    private LocalDateTime admissionDate;

    @Column(length = 255)
    private String notes;

}
