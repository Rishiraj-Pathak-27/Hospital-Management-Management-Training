import java.sql.*;

public class CheckIDs {
    public static void main(String[] args) throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection(
            "jdbc:postgresql://ep-wandering-brook-aphzk2fq-pooler.c-7.us-east-1.aws.neon.tech:5432/neondb?sslmode=require&channel_binding=require",
            "neondb_owner", "npg_HRmUr7Bnugc2");
        
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT wardId, wardName FROM wards");
        System.out.println("=== Ward IDs ===");
        while (rs.next()) System.out.println(rs.getInt(1) + " -> " + rs.getString(2));
        stmt.close();
        conn.close();
    }
}
