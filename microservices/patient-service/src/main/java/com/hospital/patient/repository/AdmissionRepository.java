package com.hospital.patient.repository;

import com.hospital.patient.entity.AdmissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionRepository extends JpaRepository<AdmissionEntity, Integer> {
}
