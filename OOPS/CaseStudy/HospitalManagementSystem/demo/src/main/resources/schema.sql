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
    `date` VARCHAR(50) NOT NULL,
    CONSTRAINT fk_appointments_patient
        FOREIGN KEY (patientId) REFERENCES patients(patientId)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_appointments_doctor
        FOREIGN KEY (doctorId) REFERENCES doctors(doctorId)
        ON DELETE CASCADE ON UPDATE CASCADE
);