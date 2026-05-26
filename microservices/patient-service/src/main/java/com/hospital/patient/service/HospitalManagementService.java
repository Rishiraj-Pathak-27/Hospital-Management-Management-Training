package com.hospital.patient.service;

import com.hospital.patient.dto.AdmissionRequest;
import com.hospital.patient.dto.AdmissionResponse;
import com.hospital.patient.dto.AppointmentRequest;
import com.hospital.patient.dto.AppointmentResponse;
import com.hospital.patient.dto.BedResponse;
import com.hospital.patient.dto.DoctorRequest;
import com.hospital.patient.dto.DoctorResponse;
import com.hospital.patient.dto.PatientRequest;
import com.hospital.patient.dto.PatientResponse;
import com.hospital.patient.dto.WardResponse;
import com.hospital.patient.entity.AdmissionEntity;
import com.hospital.patient.entity.AppointmentEntity;
import com.hospital.patient.entity.BedEntity;
import com.hospital.patient.entity.DoctorEntity;
import com.hospital.patient.entity.PatientEntity;
import com.hospital.patient.entity.WardEntity;
import com.hospital.patient.exception.DuplicateResourceException;
import com.hospital.patient.exception.NoAvailableBedException;
import com.hospital.patient.exception.ResourceNotFoundException;
import com.hospital.patient.repository.AdmissionRepository;
import com.hospital.patient.repository.AppointmentRepository;
import com.hospital.patient.repository.BedRepository;
import com.hospital.patient.repository.DoctorRepository;
import com.hospital.patient.repository.PatientRepository;
import com.hospital.patient.repository.WardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Transactional
public class HospitalManagementService {

    private static final String STATUS_CONSULT_ONLY = "CONSULT_ONLY";
    private static final String STATUS_ADMITTED = "ADMITTED";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final WardRepository wardRepository;
    private final BedRepository bedRepository;
    private final AdmissionRepository admissionRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeWardData() {
        if (wardRepository.count() > 0) {
            return;
        }

        createWardWithBeds("Critical Care Ward", "ICU", 1, 2);
        createWardWithBeds("Emergency Ward", "Emergency", 2, 3);
        createWardWithBeds("General Ward", "General", 3, 5);
    }

    public PatientResponse addPatient(PatientRequest request) {
        if (patientRepository.existsById(request.patientId())) {
            throw new DuplicateResourceException("Patient already exists");
        }

        PatientEntity patient = patientRepository.save(PatientEntity.builder()
                .patientId(request.patientId())
                .name(request.name())
                .age(request.age())
                .disease(request.disease())
                .build());

        return toPatientResponse(patient);
    }

    public List<PatientResponse> viewPatients() {
        return patientRepository.findAll(Sort.by(Sort.Order.asc("patientId")))
                .stream()
                .map(this::toPatientResponse)
                .toList();
    }

    public DoctorResponse addDoctor(DoctorRequest request) {
        if (doctorRepository.existsById(request.doctorId())) {
            throw new DuplicateResourceException("Doctor already exists");
        }

        DoctorEntity doctor = doctorRepository.save(DoctorEntity.builder()
                .doctorId(request.doctorId())
                .name(request.name())
                .specialization(request.specialization())
                .build());

        return toDoctorResponse(doctor);
    }

    public List<DoctorResponse> viewDoctors() {
        return doctorRepository.findAll(Sort.by(Sort.Order.asc("doctorId")))
                .stream()
                .map(this::toDoctorResponse)
                .toList();
    }

    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        if (appointmentRepository.existsById(request.appointmentId())) {
            throw new DuplicateResourceException("Appointment already exists");
        }

        PatientEntity patient = getPatient(request.patientId());
        DoctorEntity doctor = getDoctor(request.doctorId());

        AppointmentEntity appointment = appointmentRepository.save(AppointmentEntity.builder()
                .appointmentId(request.appointmentId())
                .patient(patient)
                .doctor(doctor)
            .appointmentDate(parseAppointmentDate(request.appointmentDate()))
                .build());

        return toAppointmentResponse(appointment);
    }

    public List<AppointmentResponse> viewAppointments() {
        return appointmentRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(AppointmentEntity::getAppointmentId))
                .map(this::toAppointmentResponse)
                .toList();
    }

    public AdmissionResponse admitPatient(AdmissionRequest request) {
        PatientEntity patient = getPatient(request.patientId());
        String severity = normalizeSeverity(request.severity());

        if (isConsultOnlySeverity(severity)) {
            AdmissionEntity admission = admissionRepository.save(AdmissionEntity.builder()
                    .patient(patient)
                    .severity(severity)
                    .admissionStatus(STATUS_CONSULT_ONLY)
                    .admissionDate(LocalDateTime.now())
                    .notes(request.notes())
                    .build());

            return toAdmissionResponse(admission);
        }

        String wardType = resolveWardType(severity);
        BedEntity bed = bedRepository.findFirstByAvailableTrueAndWard_WardTypeIgnoreCaseOrderByBedIdAsc(wardType)
                .orElseThrow(() -> new NoAvailableBedException("No available ward/bed found for this severity"));

        bed.setAvailable(false);
        bedRepository.save(bed);

        AdmissionEntity admission = admissionRepository.save(AdmissionEntity.builder()
                .patient(patient)
                .severity(severity)
                .admissionStatus(STATUS_ADMITTED)
                .ward(bed.getWard())
                .bed(bed)
            .admissionDate(LocalDateTime.now())
                .notes(request.notes())
                .build());

        return toAdmissionResponse(admission);
    }

    public List<WardResponse> viewWards() {
        return wardRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(WardEntity::getSeverityRank).thenComparing(WardEntity::getWardId))
                .map(this::toWardResponse)
                .toList();
    }

    public List<BedResponse> viewBeds() {
        return bedRepository.findAll()
                .stream()
                .sorted(Comparator.comparing((BedEntity bed) -> bed.getWard().getSeverityRank())
                        .thenComparing(BedEntity::getBedId))
                .map(this::toBedResponse)
                .toList();
    }

    public List<AdmissionResponse> viewAdmissions() {
        return admissionRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(AdmissionEntity::getAdmissionId).reversed())
                .map(this::toAdmissionResponse)
                .toList();
    }

    private void createWardWithBeds(String wardName, String wardType, int severityRank, int totalBeds) {
        WardEntity ward = wardRepository.save(WardEntity.builder()
                .wardName(wardName)
                .wardType(wardType)
                .severityRank(severityRank)
                .totalBeds(totalBeds)
                .build());

        for (int index = 1; index <= totalBeds; index++) {
            bedRepository.save(BedEntity.builder()
                    .ward(ward)
                    .bedNumber(wardType + "-" + index)
                    .available(true)
                    .build());
        }
    }

    private PatientEntity getPatient(Integer patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
    }

    private DoctorEntity getDoctor(Integer doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
    }

    private PatientResponse toPatientResponse(PatientEntity patient) {
        return new PatientResponse(patient.getPatientId(), patient.getName(), patient.getAge(), patient.getDisease());
    }

    private DoctorResponse toDoctorResponse(DoctorEntity doctor) {
        return new DoctorResponse(doctor.getDoctorId(), doctor.getName(), doctor.getSpecialization());
    }

    private AppointmentResponse toAppointmentResponse(AppointmentEntity appointment) {
        return new AppointmentResponse(
                appointment.getAppointmentId(),
                appointment.getPatient().getPatientId(),
                appointment.getPatient().getName(),
                appointment.getPatient().getAge(),
                appointment.getPatient().getDisease(),
                appointment.getDoctor().getDoctorId(),
                appointment.getDoctor().getName(),
                appointment.getDoctor().getSpecialization(),
                appointment.getAppointmentDate().format(DATE_TIME_FORMATTER)
        );
    }

    private WardResponse toWardResponse(WardEntity ward) {
        return new WardResponse(
                ward.getWardId(),
                ward.getWardName(),
                ward.getWardType(),
                ward.getSeverityRank(),
                ward.getTotalBeds()
        );
    }

    private BedResponse toBedResponse(BedEntity bed) {
        return new BedResponse(
                bed.getBedId(),
                bed.getBedNumber(),
                bed.getAvailable(),
                bed.getWard().getWardId(),
                bed.getWard().getWardName(),
                bed.getWard().getWardType()
        );
    }

    private AdmissionResponse toAdmissionResponse(AdmissionEntity admission) {
        return new AdmissionResponse(
                admission.getAdmissionId(),
                admission.getAdmissionDate().format(DATE_TIME_FORMATTER),
                admission.getSeverity(),
                admission.getAdmissionStatus(),
                admission.getPatient().getPatientId(),
                admission.getPatient().getName(),
                admission.getPatient().getAge(),
                admission.getPatient().getDisease(),
                admission.getWard() == null ? "N/A" : admission.getWard().getWardName(),
                admission.getWard() == null ? "N/A" : admission.getWard().getWardType(),
                admission.getBed() == null ? "N/A" : admission.getBed().getBedNumber(),
                admission.getNotes()
        );
    }

    private LocalDateTime parseAppointmentDate(String appointmentDate) {
        if (appointmentDate == null || appointmentDate.isBlank()) {
            return LocalDateTime.now();
        }

        return LocalDateTime.parse(appointmentDate.trim(), DATE_TIME_FORMATTER);
    }

    private boolean isConsultOnlySeverity(String severity) {
        return severity.contains("low") || severity.contains("mild") || severity.contains("minor");
    }

    private String resolveWardType(String severity) {
        if (severity.contains("critical")) {
            return "ICU";
        }

        if (severity.contains("high") || severity.contains("severe")) {
            return "Emergency";
        }

        return "General";
    }

    private String normalizeSeverity(String severity) {
        return severity == null ? "" : severity.trim().toLowerCase(Locale.ROOT);
    }

}
