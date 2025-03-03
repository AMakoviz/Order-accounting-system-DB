package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    private static Connection connection;

    public static Connection getConnection() {
        ApplicationProperties properties = ApplicationProperties.getInstance();
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                        properties.getUrl(),
                        properties.getUser(),
                        properties.getPassword()
                );
            }
        } catch (SQLException e) {
        }
        return connection;
    }

}
