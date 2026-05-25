import java.sql.*;

public class VerifyNeon {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://ep-wandering-brook-aphzk2fq-pooler.c-7.us-east-1.aws.neon.tech:5432/neondb?sslmode=require&channel_binding=require";
        String user = "neondb_owner";
        String password = "npg_HRmUr7Bnugc2";
        
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            
            Statement stmt = conn.createStatement();
            
            System.out.println("=== Database Contents ===\n");
            
            // Check wards
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM wards");
            if (rs.next()) System.out.println("Wards count: " + rs.getInt(1));
            
            // Check beds
            rs = stmt.executeQuery("SELECT COUNT(*) FROM beds");
            if (rs.next()) System.out.println("Beds count: " + rs.getInt(1));
            
            // Check patients
            rs = stmt.executeQuery("SELECT COUNT(*) FROM patients");
            if (rs.next()) System.out.println("Patients count: " + rs.getInt(1));
            
            // Check doctors
            rs = stmt.executeQuery("SELECT COUNT(*) FROM doctors");
            if (rs.next()) System.out.println("Doctors count: " + rs.getInt(1));
            
            // Check appointments
            rs = stmt.executeQuery("SELECT COUNT(*) FROM appointments");
            if (rs.next()) System.out.println("Appointments count: " + rs.getInt(1));
            
            // Check admissions
            rs = stmt.executeQuery("SELECT COUNT(*) FROM admissions");
            if (rs.next()) System.out.println("Admissions count: " + rs.getInt(1));
            
            System.out.println("\n=== Sample Wards ===");
            rs = stmt.executeQuery("SELECT ward_id, ward_name, ward_type FROM wards LIMIT 3");
            while (rs.next()) {
                System.out.println(rs.getInt(1) + " | " + rs.getString(2) + " | " + rs.getString(3));
            }
            
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
