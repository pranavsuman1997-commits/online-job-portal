import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Central place for the MySQL JDBC connection.
 * Update the database username/password to match your local MySQL setup.
 */
public class DBConnection {
    private static final String URL =
            "jdbc:mysql://localhost:3306/online_job_portal?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "YOUR_MYSQL_PASSWORD";

    private DBConnection() { }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
