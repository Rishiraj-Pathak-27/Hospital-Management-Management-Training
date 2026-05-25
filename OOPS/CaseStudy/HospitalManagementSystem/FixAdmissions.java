import java.sql.*;

public class FixAdmissions {
    public static void main(String[] args) throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection(
            "jdbc:postgresql://ep-wandering-brook-aphzk2fq-pooler.c-7.us-east-1.aws.neon.tech:5432/neondb?sslmode=require&channel_binding=require",
            "neondb_owner", "npg_HRmUr7Bnugc2");
        
        Statement stmt = conn.createStatement();
        
        // Get actual bed IDs
        System.out.println("=== Available Beds ===");
        ResultSet rs = stmt.executeQuery("SELECT bedId, wardId, bedNumber FROM beds ORDER BY bedId");
        int bed1 = 0, bed8 = 0, bed13 = 0;
        int idx = 0;
        while (rs.next()) {
            int bedId = rs.getInt(1);
            int wardId = rs.getInt(2);
            String bedNum = rs.getString(3);
            System.out.println(bedId + " (ward " + wardId + ") " + bedNum);
            idx++;
            if (idx == 1) bed1 = bedId;  // First bed (for CRITICAL admission)
            if (wardId == 8 && bed8 == 0) bed8 = bedId;  // First CARDIAC bed
            if (idx ==13) bed13 = bedId;  // 13th bed (for ORTHO admission)
        }
        
        // Delete existing admissions
        stmt.execute("DELETE FROM admissions");
        
        System.out.println("\nInserting admissions with correct bed IDs...");
        String[] admissionQueries = {
            "INSERT INTO admissions (patientId, severity, admissionStatus, wardId, bedId, admissionDate, notes) VALUES (101, 'HIGH', 'ACTIVE', 8, " + bed8 + ", '2025-05-26', 'Cardiac emergency')",
            "INSERT INTO admissions (patientId, severity, admissionStatus, wardId, bedId, admissionDate, notes) VALUES (102, 'CRITICAL', 'ACTIVE', 6, " + bed1 + ", '2025-05-25', 'Severe pneumonia')",
            "INSERT INTO admissions (patientId, severity, admissionStatus, wardId, bedId, admissionDate, notes) VALUES (105, 'MODERATE', 'ACTIVE', 10, " + bed13 + ", '2025-05-20', 'Fracture treatment')"
        };
        
        for (String query : admissionQueries) {
            System.out.println("  Executing: " + query);
            stmt.execute(query);
        }
        System.out.println("✓ Inserted 3 admissions");
        System.out.println("\n✓✓✓ ALL DATA READY! ✓✓✓\nDatabase is fully seeded. Refresh the GUI or API.");
        
        stmt.close();
        conn.close();
    }
}
