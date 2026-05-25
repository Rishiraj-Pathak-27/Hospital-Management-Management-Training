import java.sql.*;

public class SeedNeon {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://ep-wandering-brook-aphzk2fq-pooler.c-7.us-east-1.aws.neon.tech:5432/neondb?sslmode=require&channel_binding=require";
        String user = "neondb_owner";
        String password = "npg_HRmUr7Bnugc2";
        
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✓ Connected to Neon");
            
            Statement stmt = conn.createStatement();
            
            // Clear existing data
            try {
                System.out.println("Clearing existing data...");
                stmt.execute("DELETE FROM admissions");
                stmt.execute("DELETE FROM beds");
                stmt.execute("DELETE FROM wards");
                stmt.execute("DELETE FROM appointments");
                stmt.execute("DELETE FROM doctors");
                stmt.execute("DELETE FROM patients");
            } catch (SQLException e) {
                // Tables might not exist yet
            }
            
            // Insert Wards with correct column names
            String[] wardQueries = {
                "INSERT INTO wards (wardName, wardType, severityRank, totalBeds) VALUES ('ICU Ward', 'CRITICAL', 1, 10)",
                "INSERT INTO wards (wardName, wardType, severityRank, totalBeds) VALUES ('General Ward', 'GENERAL', 3, 25)",
                "INSERT INTO wards (wardName, wardType, severityRank, totalBeds) VALUES ('Cardiology Ward', 'CARDIAC', 2, 15)",
                "INSERT INTO wards (wardName, wardType, severityRank, totalBeds) VALUES ('Pediatric Ward', 'PEDIATRIC', 2, 20)",
                "INSERT INTO wards (wardName, wardType, severityRank, totalBeds) VALUES ('Orthopedic Ward', 'ORTHOPEDIC', 3, 18)"
            };
            
            for (String query : wardQueries) {
                try {
                    stmt.execute(query);
                } catch (SQLException e) {
                    System.err.println("Ward insert error: " + e.getMessage());
                }
            }
            System.out.println("✓ Inserted 5 wards");
            
            // Insert Beds with correct column names
            String[] bedQueries = {
                "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (1, 'ICU-01', false)",
                "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (1, 'ICU-02', false)",
                "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (1, 'ICU-03', true)",
                "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (1, 'ICU-04', true)",
                "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (2, 'GEN-01', false)",
                "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (2, 'GEN-02', true)",
                "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (2, 'GEN-03', true)",
                "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (3, 'CARD-01', false)",
                "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (3, 'CARD-02', true)",
                "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (3, 'CARD-03', true)",
                "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (4, 'PED-01', false)",
                "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (4, 'PED-02', true)",
                "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (5, 'ORTHO-01', true)",
                "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (5, 'ORTHO-02', true)"
            };
            
            for (String query : bedQueries) {
                try {
                    stmt.execute(query);
                } catch (SQLException e) {
                    System.err.println("Bed insert error: " + e.getMessage());
                }
            }
            System.out.println("✓ Inserted 14 beds");
            
            // Insert Patients with correct column names
            String[] patientQueries = {
                "INSERT INTO patients (patientId, name, age, disease) VALUES (101, 'John Doe', 45, 'Heart Disease')",
                "INSERT INTO patients (patientId, name, age, disease) VALUES (102, 'Jane Smith', 38, 'Pneumonia')",
                "INSERT INTO patients (patientId, name, age, disease) VALUES (103, 'Robert Brown', 62, 'Hypertension')",
                "INSERT INTO patients (patientId, name, age, disease) VALUES (104, 'Emily Johnson', 28, 'Asthma')",
                "INSERT INTO patients (patientId, name, age, disease) VALUES (105, 'Michael Davis', 55, 'Fracture')",
                "INSERT INTO patients (patientId, name, age, disease) VALUES (106, 'Sarah Wilson', 42, 'Diabetes')"
            };
            
            for (String query : patientQueries) {
                try {
                    stmt.execute(query);
                } catch (SQLException e) {
                    System.err.println("Patient insert error: " + e.getMessage());
                }
            }
            System.out.println("✓ Inserted 6 patients");
            
            // Insert Doctors with correct column names
            String[] doctorQueries = {
                "INSERT INTO doctors (doctorId, name, specialization) VALUES (201, 'Dr. Rajesh Kumar', 'CARDIOLOGY')",
                "INSERT INTO doctors (doctorId, name, specialization) VALUES (202, 'Dr. Priya Sharma', 'PEDIATRICS')",
                "INSERT INTO doctors (doctorId, name, specialization) VALUES (203, 'Dr. Amit Patel', 'ORTHOPEDICS')",
                "INSERT INTO doctors (doctorId, name, specialization) VALUES (204, 'Dr. Neha Gupta', 'GENERAL')",
                "INSERT INTO doctors (doctorId, name, specialization) VALUES (205, 'Dr. Vikram Singh', 'CRITICAL_CARE')"
            };
            
            for (String query : doctorQueries) {
                try {
                    stmt.execute(query);
                } catch (SQLException e) {
                    System.err.println("Doctor insert error: " + e.getMessage());
                }
            }
            System.out.println("✓ Inserted 5 doctors");
            
            // Insert Appointments with correct column names
            String[] appointmentQueries = {
                "INSERT INTO appointments (appointmentId, patientId, doctorId, appointment_date) VALUES (301, 101, 201, '2025-05-26')",
                "INSERT INTO appointments (appointmentId, patientId, doctorId, appointment_date) VALUES (302, 102, 204, '2025-05-27')",
                "INSERT INTO appointments (appointmentId, patientId, doctorId, appointment_date) VALUES (303, 103, 204, '2025-05-28')",
                "INSERT INTO appointments (appointmentId, patientId, doctorId, appointment_date) VALUES (304, 104, 202, '2025-05-29')",
                "INSERT INTO appointments (appointmentId, patientId, doctorId, appointment_date) VALUES (305, 105, 203, '2025-05-30')",
                "INSERT INTO appointments (appointmentId, patientId, doctorId, appointment_date) VALUES (306, 106, 204, '2025-05-31')"
            };
            
            for (String query : appointmentQueries) {
                try {
                    stmt.execute(query);
                } catch (SQLException e) {
                    System.err.println("Appointment insert error: " + e.getMessage());
                }
            }
            System.out.println("✓ Inserted 6 appointments");
            
            // Insert Admissions with correct column names
            String[] admissionQueries = {
                "INSERT INTO admissions (patientId, severity, admissionStatus, wardId, bedId, admissionDate, notes) VALUES (101, 'HIGH', 'ACTIVE', 3, 8, '2025-05-26', 'Cardiac emergency')",
                "INSERT INTO admissions (patientId, severity, admissionStatus, wardId, bedId, admissionDate, notes) VALUES (102, 'CRITICAL', 'ACTIVE', 1, 1, '2025-05-25', 'Severe pneumonia')",
                "INSERT INTO admissions (patientId, severity, admissionStatus, wardId, bedId, admissionDate, notes) VALUES (105, 'MODERATE', 'ACTIVE', 5, 12, '2025-05-20', 'Fracture treatment')"
            };
            
            for (String query : admissionQueries) {
                try {
                    stmt.execute(query);
                } catch (SQLException e) {
                    System.err.println("Admission insert error: " + e.getMessage());
                }
            }
            System.out.println("✓ Inserted 3 admissions");
            
            System.out.println("\n✓✓✓ Neon database seeded successfully! ✓✓✓");
            System.out.println("GUI is now ready to display data and process admissions.");
            
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
