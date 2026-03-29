package util;
// package util: nơi chứa các lớp tiện ích (utility), ví dụ DBConnection

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Lớp DBConnection quản lý việc kết nối tới MySQL
public class DBConnection {
    private static DBConnection instance; // Singleton instance: chỉ tạo 1 đối tượng duy nhất
    private Connection connection;        // Biến lưu trữ kết nối JDBC

    // Thông tin kết nối
    private static final String URL = "jdbc:mysql://localhost:3306/phone_store?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "dUYDEP24022006";

    // Constructor private: không cho tạo đối tượng từ bên ngoài
    private DBConnection() {
        try {
            // Load driver MySQL (không bắt buộc với JDBC 4+, nhưng thêm vào cho chắc chắn)
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Tạo kết nối tới DB
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            // Nếu không tìm thấy driver
            System.out.println("Không tìm thấy driver MySQL");
            e.printStackTrace();
        } catch (SQLException e) {
            // Nếu kết nối DB thất bại
            System.out.println("Kết nối DB thất bại");
            e.printStackTrace();
        }
    }

    // Phương thức getInstance: đảm bảo chỉ có 1 instance duy nhất (Singleton)
    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    // Trả về đối tượng Connection để DAO sử dụng
    public Connection getConnection() {
        return connection;
    }

    // Hàm testQuery: kiểm tra kết nối có thành công không
    public void testQuery() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Kết nối thành công");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi DB");
            e.printStackTrace();
        }
    }
}
