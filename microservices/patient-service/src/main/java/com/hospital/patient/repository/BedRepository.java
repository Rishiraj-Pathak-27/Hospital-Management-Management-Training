package com.hospital.patient.repository;

import com.hospital.patient.entity.BedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BedRepository extends JpaRepository<BedEntity, Integer> {

    Optional<BedEntity> findFirstByAvailableTrueAndWard_WardTypeIgnoreCaseOrderByBedIdAsc(String wardType);

}
