package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection() throws SQLException {
        // Update with your database URL, username, and password
        String url = "jdbc:mysql://localhost:3306/student";
        String user = "root";
        String password = "";

        // Establish a connection and return it
        return DriverManager.getConnection(url, user, password);
    }
}
