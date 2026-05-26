CREATE DATABASE IF NOT EXISTS patientdb;
USE patientdb;

DROP TABLE IF EXISTS admissions;
DROP TABLE IF EXISTS beds;
DROP TABLE IF EXISTS wards;
DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS doctors;
DROP TABLE IF EXISTS patients;

CREATE TABLE patients (
    patientId INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    disease VARCHAR(100) NOT NULL
);

CREATE TABLE doctors (
    doctorId INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100) NOT NULL
);

CREATE TABLE appointments (
    appointmentId INT PRIMARY KEY,
    patientId INT NOT NULL,
    doctorId INT NOT NULL,
    appointment_date DATETIME NOT NULL,
    CONSTRAINT fk_appointments_patient
        FOREIGN KEY (patientId) REFERENCES patients(patientId)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_appointments_doctor
        FOREIGN KEY (doctorId) REFERENCES doctors(doctorId)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE wards (
    wardId INT AUTO_INCREMENT PRIMARY KEY,
    wardName VARCHAR(100) NOT NULL,
    wardType VARCHAR(50) NOT NULL,
    severityRank INT NOT NULL,
    totalBeds INT NOT NULL,
    UNIQUE KEY uk_ward_name (wardName)
);

CREATE TABLE beds (
    bedId INT AUTO_INCREMENT PRIMARY KEY,
    wardId INT NOT NULL,
    bedNumber VARCHAR(30) NOT NULL,
    isAvailable BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE KEY uk_ward_bed_number (wardId, bedNumber),
    CONSTRAINT fk_beds_ward
        FOREIGN KEY (wardId) REFERENCES wards(wardId)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE admissions (
    admissionId INT AUTO_INCREMENT PRIMARY KEY,
    patientId INT NOT NULL,
    severity VARCHAR(20) NOT NULL,
    admissionStatus VARCHAR(30) NOT NULL,
    wardId INT NULL,
    bedId INT NULL,
    admissionDate DATETIME NOT NULL,
    notes VARCHAR(255) NULL,
    CONSTRAINT fk_admissions_patient
        FOREIGN KEY (patientId) REFERENCES patients(patientId)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_admissions_ward
        FOREIGN KEY (wardId) REFERENCES wards(wardId)
        ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_admissions_bed
        FOREIGN KEY (bedId) REFERENCES beds(bedId)
        ON DELETE SET NULL ON UPDATE CASCADE
);

INSERT INTO wards (wardName, wardType, severityRank, totalBeds) VALUES
('Critical Care Ward', 'ICU', 1, 2),
('Emergency Ward', 'Emergency', 2, 3),
('General Ward', 'General', 3, 5);

INSERT INTO beds (wardId, bedNumber, isAvailable)
SELECT w.wardId, CONCAT(w.wardType, '-', n.n), TRUE
FROM wards w
JOIN (
    SELECT 1 AS n UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5
) n
ON n.n <= w.totalBeds;
