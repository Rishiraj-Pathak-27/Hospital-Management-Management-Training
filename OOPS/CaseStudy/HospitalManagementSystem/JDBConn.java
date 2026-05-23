import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBConn {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // If the driver isn't on the classpath this will fail early with a clear error
            throw new ExceptionInInitializerError(e);
        }
    }

    static Connection getConnection() throws SQLException {

        String url = "jdbc:mysql://localhost:3306/hmsdb";
        String username = "root";
        String password = "Root@1234";

        return DriverManager.getConnection(url,username,password);
    }
}