CREATE TABLE IF NOT EXISTS patients (
    patientId INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    disease VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS doctors (
    doctorId INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS appointments (
    appointmentId INT PRIMARY KEY,
    patientId INT NOT NULL,
    doctorId INT NOT NULL,
    appointment_date VARCHAR(50) NOT NULL,
    CONSTRAINT fk_appointments_patient
        FOREIGN KEY (patientId) REFERENCES patients(patientId)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_appointments_doctor
        FOREIGN KEY (doctorId) REFERENCES doctors(doctorId)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS wards (
    wardId SERIAL PRIMARY KEY,
    wardName VARCHAR(100) NOT NULL,
    wardType VARCHAR(50) NOT NULL,
    severityRank INT NOT NULL,
    totalBeds INT NOT NULL,
    UNIQUE (wardName)
);

CREATE TABLE IF NOT EXISTS beds (
    bedId SERIAL PRIMARY KEY,
    wardId INT NOT NULL,
    bedNumber VARCHAR(30) NOT NULL,
    isAvailable BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE (wardId, bedNumber),
    CONSTRAINT fk_beds_ward
        FOREIGN KEY (wardId) REFERENCES wards(wardId)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS admissions (
    admissionId SERIAL PRIMARY KEY,
    patientId INT NOT NULL,
    severity VARCHAR(20) NOT NULL,
    admissionStatus VARCHAR(30) NOT NULL,
    wardId INT NULL,
    bedId INT NULL,
    admissionDate VARCHAR(50) NOT NULL,
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