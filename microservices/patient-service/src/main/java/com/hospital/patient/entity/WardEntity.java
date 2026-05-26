package com.hospital.patient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wardId")
    private Integer wardId;

    @Column(nullable = false, unique = true)
    private String wardName;

    @Column(nullable = false)
    private String wardType;

    @Column(nullable = false)
    private Integer severityRank;

    @Column(nullable = false)
    private Integer totalBeds;

    @Builder.Default
    @OneToMany(mappedBy = "ward")
    private List<BedEntity> beds = new ArrayList<>();

}
