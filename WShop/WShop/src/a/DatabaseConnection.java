package a;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/test"; // Đổi "testdb" thành tên database của bạn
    private static final String USER = "postgres"; // Tên đăng nhập PostgreSQL
    private static final String PASSWORD = "toughgirlmask2912"; // Mật khẩu PostgreSQL

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Kết nối thất bại: " + e.getMessage());
            return null;
        }
    }
}
