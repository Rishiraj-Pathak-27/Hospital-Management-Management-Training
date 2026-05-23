package com.example.demo.repository;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class HospitalRepository {

    private final JdbcTemplate jdbcTemplate;

    public HospitalRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long countPatients() {
        return countTable("patients");
    }

    public long countDoctors() {
        return countTable("doctors");
    }

    public long countAppointments() {
        return countTable("appointments");
    }

    public Map<String, Long> fetchAllCounts() {
        Map<String, Long> counts = new LinkedHashMap<>();
        counts.put("patients", countPatients());
        counts.put("doctors", countDoctors());
        counts.put("appointments", countAppointments());
        counts.put("totalRecords", counts.values().stream().mapToLong(Long::longValue).sum());
        return counts;
    }

    private long countTable(String tableName) {
        Long count = jdbcTemplate.queryForObject("select count(*) from " + tableName, Long.class);
        return count != null ? count : 0L;
    }
}