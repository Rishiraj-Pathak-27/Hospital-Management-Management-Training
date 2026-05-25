package com.example.demo.repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.demo.dto.WardDto;
import com.example.demo.dto.BedDto;
import com.example.demo.dto.AdmissionDto;

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

    public List<WardDto> fetchWards() {
        String sql = "select wardId, wardName, wardType, severityRank, totalBeds from wards order by severityRank";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new WardDto(
                rs.getInt("wardId"),
                rs.getString("wardName"),
                rs.getString("wardType"),
                rs.getInt("severityRank"),
                rs.getInt("totalBeds")
        ));
    }

    public List<BedDto> fetchBeds() {
        String sql = "select b.bedId, b.wardId, b.bedNumber, b.isAvailable, w.wardName, w.wardType from beds b join wards w on b.wardId = w.wardId order by w.severityRank, b.bedId";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new BedDto(
                rs.getInt("bedId"),
                rs.getInt("wardId"),
                rs.getString("bedNumber"),
                rs.getBoolean("isAvailable"),
                rs.getString("wardName"),
                rs.getString("wardType")
        ));
    }

    public List<AdmissionDto> fetchAdmissions() {
        String sql = "select a.admissionId, a.patientId, a.severity, a.admissionStatus, a.admissionDate, a.wardId, a.bedId, w.wardName, b.bedNumber, a.notes from admissions a left join wards w on a.wardId = w.wardId left join beds b on a.bedId = b.bedId order by a.admissionId desc";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new AdmissionDto(
                rs.getInt("admissionId"),
                rs.getInt("patientId"),
                rs.getString("severity"),
                rs.getString("admissionStatus"),
                rs.getString("admissionDate"),
                rs.getObject("wardId") == null ? null : rs.getInt("wardId"),
                rs.getObject("bedId") == null ? null : rs.getInt("bedId"),
                rs.getString("wardName"),
                rs.getString("bedNumber"),
                rs.getString("notes")
        ));
    }

    public Optional<BedDto> findAvailableBedByWardType(String wardType) {
        String sql = "select b.bedId, b.wardId, b.bedNumber, b.isAvailable, w.wardName, w.wardType from beds b join wards w on b.wardId = w.wardId where b.isAvailable = true and lower(w.wardType) = ? limit 1";
        List<BedDto> rows = jdbcTemplate.query(sql, new Object[] { wardType.toLowerCase() }, (rs, rn) -> new BedDto(
                rs.getInt("bedId"), rs.getInt("wardId"), rs.getString("bedNumber"), rs.getBoolean("isAvailable"), rs.getString("wardName"), rs.getString("wardType")
        ));
        return rows.isEmpty() ? Optional.empty() : Optional.of(rows.get(0));
    }

    public int markBedUnavailable(int bedId) {
        return jdbcTemplate.update("update beds set isAvailable = false where bedId = ?", bedId);
    }

    public int insertAdmission(int patientId, String severity, String status, Integer wardId, Integer bedId, String date, String notes) {
        return jdbcTemplate.update("insert into admissions (patientId, severity, admissionStatus, wardId, bedId, admissionDate, notes) values (?,?,?,?,?,?,?)",
                patientId, severity, status, wardId, bedId, date, notes);
    }

    public boolean patientExists(int patientId) {
        Integer count = jdbcTemplate.queryForObject("select count(*) from patients where patientId = ?", Integer.class, patientId);
        return count != null && count > 0;
    }
}