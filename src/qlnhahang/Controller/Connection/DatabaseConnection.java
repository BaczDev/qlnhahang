package qlnhahang.Controller.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private DatabaseConnection() {
        // Constructor riêng để đảm bảo Singleton
    }

    // Thực hiện kết nối tới MySQL Database
    public void connectToDatabase() throws SQLException {
        final String url = "jdbc:mysql://localhost:3306/test"; // Thay bằng tên DB của bạn
        final String username = "root"; // Thay bằng username MySQL
        final String password = "bac22111"; // Thay bằng mật khẩu MySQL

        try {
            // Nạp driver MySQL (cần thiết với một số trình biên dịch cũ)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Không tìm thấy MySQL JDBC Driver", e);
        }

        connection = DriverManager.getConnection(url, username, password);
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
