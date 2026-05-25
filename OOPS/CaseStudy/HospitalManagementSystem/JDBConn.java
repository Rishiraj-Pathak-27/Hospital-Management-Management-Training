import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBConn {
    static {
        // Try to load both MySQL and PostgreSQL drivers
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // MySQL driver not available, will try Postgres
        }
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            // Postgres driver not available, will try MySQL
        }
    }

    static Connection getConnection() throws SQLException {
        // Prioritize SPRING_DATASOURCE_* (for Neon/Postgres), then DATABASE_* (for MySQL)
        String url = System.getenv().getOrDefault(
                "SPRING_DATASOURCE_URL",
                System.getenv().getOrDefault(
                    "DATABASE_URL",
                    "jdbc:mysql://localhost:3306/hmsdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
                )
        );
        String username = System.getenv().getOrDefault(
                "SPRING_DATASOURCE_USERNAME",
                System.getenv().getOrDefault("DATABASE_USER", "root")
        );
        String password = System.getenv().getOrDefault(
                "SPRING_DATASOURCE_PASSWORD",
                System.getenv().getOrDefault("DATABASE_PASSWORD", "Root@1234")
        );

        return DriverManager.getConnection(url, username, password);
    }
}