package a;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductDAO {
    // Thêm sản phẩm vào bảng
    public void addProduct(String name, double price, int quantity) {
        String sql = "INSERT INTO products (name, price, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setInt(3, quantity);
            stmt.executeUpdate();
            System.out.println("Thêm sản phẩm thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hiển thị tất cả sản phẩm
    public void getAllProducts() {
        String sql = "SELECT * FROM products";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Price: %.2f | Quantity: %d%n",
                        rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getInt("quantity"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

