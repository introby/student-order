package student_order.dao;

import student_order.config.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionBuilder {
    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(Config.getProperties(Config.DB_URL), Config.getProperties(Config.DB_LOGIN), Config.getProperties(Config.DB_PASSWORD));
        return connection;
    }
}
