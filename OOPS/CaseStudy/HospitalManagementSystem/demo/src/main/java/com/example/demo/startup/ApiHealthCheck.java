package com.example.demo.startup;

import com.example.demo.repository.HospitalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApiHealthCheck implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApiHealthCheck.class);
    private final HospitalRepository hospitalRepository;

    public ApiHealthCheck(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String port = System.getenv().getOrDefault("PORT", "8080");
        logger.info("========================================");
        logger.info("Hospital Management System API Health Check");
        logger.info("========================================");
        logger.info("API Status: ✓ RUNNING");
        logger.info("Base URL: http://localhost:" + port + "/api/hospital");
        logger.info("");
        logger.info("Available Endpoints:");
        logger.info("  1. GET /hello - Health check endpoint");
        logger.info("  2. GET /counts - Get all entity counts");
        logger.info("  3. GET /counts/patients - Get patient count");
        logger.info("  4. GET /counts/doctors - Get doctor count");
        logger.info("  5. GET /counts/appointments - Get appointment count");
        logger.info("");
        
        try {
            long patientCount = hospitalRepository.countPatients();
            long doctorCount = hospitalRepository.countDoctors();
            long appointmentCount = hospitalRepository.countAppointments();
            
            logger.info("Database Connection: ✓ HEALTHY");
            logger.info("Current Database Statistics:");
            logger.info("  - Patients: " + patientCount);
            logger.info("  - Doctors: " + doctorCount);
            logger.info("  - Appointments: " + appointmentCount);
            logger.info("  - Total Records: " + (patientCount + doctorCount + appointmentCount));
        } catch (Exception e) {
            logger.error("Database Connection: ✗ FAILED");
            logger.error("Error: " + e.getMessage());
        }
        
        logger.info("========================================");
        logger.info("API is ready to accept requests!");
        logger.info("========================================");
    }
}
