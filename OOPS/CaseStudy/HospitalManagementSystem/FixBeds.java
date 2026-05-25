import java.sql.*;

public class FixBeds {
    public static void main(String[] args) throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection(
            "jdbc:postgresql://ep-wandering-brook-aphzk2fq-pooler.c-7.us-east-1.aws.neon.tech:5432/neondb?sslmode=require&channel_binding=require",
            "neondb_owner", "npg_HRmUr7Bnugc2");
        
        System.out.println("Clearing old beds and admissions...");
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE FROM admissions");
        stmt.execute("DELETE FROM beds");
        
        System.out.println("Inserting beds with correct wardIds (6-10)...");
        String[] bedQueries = {
            "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (6, 'ICU-01', false)",
            "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (6, 'ICU-02', false)",
            "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (6, 'ICU-03', true)",
            "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (6, 'ICU-04', true)",
            "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (7, 'GEN-01', false)",
            "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (7, 'GEN-02', true)",
            "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (7, 'GEN-03', true)",
            "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (8, 'CARD-01', false)",
            "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (8, 'CARD-02', true)",
            "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (8, 'CARD-03', true)",
            "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (9, 'PED-01', false)",
            "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (9, 'PED-02', true)",
            "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (10, 'ORTHO-01', true)",
            "INSERT INTO beds (wardId, bedNumber, isAvailable) VALUES (10, 'ORTHO-02', true)"
        };
        
        for (String query : bedQueries) {
            stmt.execute(query);
        }
        System.out.println("✓ Inserted 14 beds");
        
        System.out.println("Inserting admissions with correct wardIds and bedIds...");
        String[] admissionQueries = {
            "INSERT INTO admissions (patientId, severity, admissionStatus, wardId, bedId, admissionDate, notes) VALUES (101, 'HIGH', 'ACTIVE', 8, 8, '2025-05-26', 'Cardiac emergency')",
            "INSERT INTO admissions (patientId, severity, admissionStatus, wardId, bedId, admissionDate, notes) VALUES (102, 'CRITICAL', 'ACTIVE', 6, 1, '2025-05-25', 'Severe pneumonia')",
            "INSERT INTO admissions (patientId, severity, admissionStatus, wardId, bedId, admissionDate, notes) VALUES (105, 'MODERATE', 'ACTIVE', 10, 13, '2025-05-20', 'Fracture treatment')"
        };
        
        for (String query : admissionQueries) {
            stmt.execute(query);
        }
        System.out.println("✓ Inserted 3 admissions");
        System.out.println("\n✓✓✓ Database fixed and ready! ✓✓✓");
        
        stmt.close();
        conn.close();
    }
}
