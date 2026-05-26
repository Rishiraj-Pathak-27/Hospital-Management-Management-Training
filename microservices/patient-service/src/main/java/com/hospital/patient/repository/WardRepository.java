package com.hospital.patient.repository;

import com.hospital.patient.entity.WardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WardRepository extends JpaRepository<WardEntity, Integer> {
}
