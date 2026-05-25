-- Seed Neon database with sample data
INSERT INTO wards (ward_id, ward_name, ward_type, capacity, current_occupancy) VALUES
(1, 'ICU Ward', 'CRITICAL', 10, 3),
(2, 'General Ward', 'GENERAL', 25, 12),
(3, 'Cardiology Ward', 'CARDIAC', 15, 8),
(4, 'Pediatric Ward', 'PEDIATRIC', 20, 10),
(5, 'Orthopedic Ward', 'ORTHOPEDIC', 18, 7)
ON CONFLICT (ward_id) DO NOTHING;

INSERT INTO beds (bed_id, ward_id, bed_number, status, admission_date) VALUES
(1, 1, 'ICU-01', 'OCCUPIED', '2025-05-20'),
(2, 1, 'ICU-02', 'OCCUPIED', '2025-05-21'),
(3, 1, 'ICU-03', 'AVAILABLE', NULL),
(4, 1, 'ICU-04', 'AVAILABLE', NULL),
(5, 2, 'GEN-01', 'OCCUPIED', '2025-05-19'),
(6, 2, 'GEN-02', 'AVAILABLE', NULL),
(7, 2, 'GEN-03', 'AVAILABLE', NULL),
(8, 3, 'CARD-01', 'OCCUPIED', '2025-05-22'),
(9, 3, 'CARD-02', 'AVAILABLE', NULL),
(10, 4, 'PED-01', 'OCCUPIED', '2025-05-18'),
(11, 4, 'PED-02', 'AVAILABLE', NULL),
(12, 5, 'ORTHO-01', 'AVAILABLE', NULL),
(13, 5, 'ORTHO-02', 'AVAILABLE', NULL)
ON CONFLICT (bed_id) DO NOTHING;

INSERT INTO patients (patient_id, patient_name, age, gender, phone, address, admission_date) VALUES
(101, 'John Doe', 45, 'M', '9876543210', '123 Main St', '2025-05-20'),
(102, 'Jane Smith', 38, 'F', '9876543211', '456 Oak Ave', '2025-05-21'),
(103, 'Robert Brown', 62, 'M', '9876543212', '789 Pine Rd', '2025-05-22'),
(104, 'Emily Johnson', 28, 'F', '9876543213', '321 Elm St', '2025-05-18'),
(105, 'Michael Davis', 55, 'M', '9876543214', '654 Maple Dr', '2025-05-19'),
(106, 'Sarah Wilson', 42, 'F', '9876543215', '987 Birch Ln', '2025-05-20')
ON CONFLICT (patient_id) DO NOTHING;

INSERT INTO doctors (doctor_id, doctor_name, specialization, phone, office_hours) VALUES
(201, 'Dr. Rajesh Kumar', 'CARDIOLOGY', '9876543220', '9:00 AM - 5:00 PM'),
(202, 'Dr. Priya Sharma', 'PEDIATRICS', '9876543221', '10:00 AM - 6:00 PM'),
(203, 'Dr. Amit Patel', 'ORTHOPEDICS', '9876543222', '8:00 AM - 4:00 PM'),
(204, 'Dr. Neha Gupta', 'GENERAL', '9876543223', '9:00 AM - 5:00 PM'),
(205, 'Dr. Vikram Singh', 'ICU SPECIALIST', '9876543224', '24/7')
ON CONFLICT (doctor_id) DO NOTHING;

INSERT INTO appointments (appointment_id, patient_id, doctor_id, appointment_date, appointment_time, purpose, status) VALUES
(301, 101, 205, '2025-05-26', '10:00', 'ICU Consultation', 'CONFIRMED'),
(302, 102, 201, '2025-05-27', '11:00', 'Cardiac Checkup', 'CONFIRMED'),
(303, 103, 203, '2025-05-28', '02:00 PM', 'Orthopedic Exam', 'PENDING'),
(304, 104, 202, '2025-05-29', '03:00 PM', 'Pediatric Checkup', 'CONFIRMED'),
(305, 105, 204, '2025-05-30', '09:00', 'General Checkup', 'CONFIRMED')
ON CONFLICT (appointment_id) DO NOTHING;

INSERT INTO admissions (admission_id, patient_id, ward_id, bed_id, doctor_id, severity, notes, admission_date, status) VALUES
(401, 101, 1, 1, 205, 'CRITICAL', 'ICU admission for post-op monitoring', '2025-05-20', 'ACTIVE'),
(402, 102, 1, 2, 205, 'CRITICAL', 'Emergency ICU admission', '2025-05-21', 'ACTIVE'),
(403, 105, 2, 5, 204, 'MODERATE', 'General ward admission for observation', '2025-05-19', 'ACTIVE'),
(404, 103, 3, 8, 201, 'HIGH', 'Cardiac care admission', '2025-05-22', 'ACTIVE')
ON CONFLICT (admission_id) DO NOTHING;

