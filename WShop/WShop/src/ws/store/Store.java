package ws.store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import ws.item.Item;
import ws.item.ItemComparatorByPriceDecrease;
import ws.item.ItemComparatorByPriceIncrease;
import ws.item.ItemComparatorByTitleDecrease;
import ws.item.ItemComparatorByTitleIncrease;
import ws.item.ItemComparatorByTypeDecrease;
import ws.item.ItemComparatorByTypeIncrease;
import ws.main.DatabaseConnection;

public class Store {
	public void addItem(Item item, String subtype, int size) {
	    String sqlInsert = "INSERT INTO item (id, title, price, type, material, subtype, size) VALUES (?, ?, ?, ?, ?, ?, ?)";

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {

	        // Set các giá trị từ đối tượng item vào PreparedStatement
	        stmt.setString(1, item.getId());
	        stmt.setString(2, item.getTitle());
	        stmt.setFloat(3, item.getPrice());
	        stmt.setString(4, item.getType());
	        stmt.setString(5, item.getMaterial());

	        // Kiểm tra và xử lý trường subtype và size dựa trên tham số truyền vào
	        if (subtype != null) {
	            stmt.setString(6, subtype); // Gán giá trị subtype nếu có
	        } else {
	            stmt.setNull(6, java.sql.Types.VARCHAR); // Gán null nếu không có subtype
	        }

	        // Kiểm tra và xử lý trường size
	        if (size > 0) {
	            stmt.setInt(7, size); // Gán giá trị size nếu hợp lệ
	        } else {
	            stmt.setNull(7, java.sql.Types.INTEGER); // Gán null nếu không có size
	        }

	        // Thực thi câu lệnh chèn
	        stmt.executeUpdate();
	        System.out.println(item.getTitle() + " has been added to the store in the database.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Failed to add item to the database.");
	    }
	}


	public boolean itemExists(String id) {
	    String sqlCheck = "SELECT 1 FROM item WHERE id = ?";
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sqlCheck)) {

	        stmt.setString(1, id);
	        try (ResultSet rs = stmt.executeQuery()) {
	            return rs.next(); // Nếu có dòng trả về, sản phẩm đã tồn tại
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	public void removeItem(String id) {
	    String sqlDelete = "DELETE FROM item WHERE id = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sqlDelete)) {
	        
	        // Set giá trị id vào câu lệnh PreparedStatement
	        stmt.setString(1, id);

	        // Thực thi câu lệnh DELETE và trả về số dòng bị ảnh hưởng
	        int rowsAffected = stmt.executeUpdate();
	        
	        if (rowsAffected > 0) {
	            System.out.println("Item with ID " + id + " has been removed from the store.");
	        } else {
	            System.out.println("Item with ID " + id + " not found in the store.");
	        }
	    } catch (SQLException e) {
	        // In ra lỗi nếu có
	        e.printStackTrace();
	        System.out.println("Failed to remove item with ID " + id + " from the database.");
	    }
	}


	public void searchByTitle(String title) {
	    String sqlSearchByTitle = """
	        SELECT id, title, price, type, material, subtype, size
	        FROM item
	        WHERE title ILIKE ?
	    """;

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sqlSearchByTitle)) {

	        stmt.setString(1, "%" + title + "%");

	        try (ResultSet rs = stmt.executeQuery()) {
	            System.out.println("Items matching title: " + title);
	            while (rs.next()) {
	                String id = rs.getString("id");
	                String itemTitle = rs.getString("title");
	                float price = rs.getFloat("price");
	                String type = rs.getString("type");
	                String material = rs.getString("material");
	                String subtype = rs.getString("subtype");
	                int size = rs.getInt("size");

	                System.out.printf("ID: %s, Title: %s, Price: %.2f, Type: %s, Material: %s, Subtype: %s, Size: %d%n",
	                        id, itemTitle, price, type, material, subtype != null ? subtype : "N/A", size > 0 ? size : 0);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Failed to search items by Title.");
	    }
	}
	public void searchById(String idPart) {
	    String sqlSearchById = """
	        SELECT i.id, i.title, i.price, i.type, i.material, i.subtype, i.size
	        FROM item i
	        WHERE i.id LIKE ?
	    """;

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sqlSearchById)) {

	        stmt.setString(1, "%" + idPart + "%");

	        try (ResultSet rs = stmt.executeQuery()) {
	            System.out.println("Items in store matching ID: " + idPart);
	            while (rs.next()) {
	                String idItem = rs.getString("id");
	                String title = rs.getString("title");
	                float price = rs.getFloat("price");
	                String type = rs.getString("type");
	                String material = rs.getString("material");
	                String subtype = rs.getString("subtype");
	                int size = rs.getInt("size");

	                System.out.printf("ID: %s, Title: %s, Price: %.2f, Type: %s, Material: %s, Subtype: %s, Size: %d%n",
	                        idItem, title, price, type, material, subtype != null ? subtype : "N/A", size > 0 ? size : 0);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Failed to search item by ID in Store.");
	    }
	}

	public void searchByType(String type) {
	    String sqlSearchByType = """
	        SELECT id, title, price, type, material, subtype, size
	        FROM item
	        WHERE type ILIKE ?
	    """;

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sqlSearchByType)) {

	        stmt.setString(1, "%" + type + "%");

	        try (ResultSet rs = stmt.executeQuery()) {
	            System.out.println("Items matching type: " + type);
	            while (rs.next()) {
	                String id = rs.getString("id");
	                String title = rs.getString("title");
	                float price = rs.getFloat("price");
	                String itemType = rs.getString("type");
	                String material = rs.getString("material");
	                String subtype = rs.getString("subtype");
	                int size = rs.getInt("size");

	                System.out.printf("ID: %s, Title: %s, Price: %.2f, Type: %s, Material: %s, Subtype: %s, Size: %d%n",
	                        id, title, price, itemType, material, subtype != null ? subtype : "N/A", size > 0 ? size : 0);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Failed to search items by Type.");
	    }
	}
    public void sortByTitle(String order) {
        String sqlSortByTitle = "SELECT * FROM item ORDER BY title " + (order.equalsIgnoreCase("ASC") ? "ASC" : "DESC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlSortByTitle);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Items sorted by Title (" + order + "):");
            while (rs.next()) {
                String id = rs.getString("id");
                String title = rs.getString("title");
                float price = rs.getFloat("price");
                String type = rs.getString("type");
                String material = rs.getString("material");
                String subtype = rs.getString("subtype");
                int size = rs.getInt("size");

                System.out.println("ID: " + id + ", Title: " + title + ", Price: " + price + ", Type: " + type +
                        ", Material: " + material + ", Subtype: " + subtype + ", Size: " + size);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to sort items by title.");
        }
    }
    public void sortByType(String order) {
        String sqlSortByType = "SELECT * FROM item ORDER BY type " + (order.equalsIgnoreCase("ASC") ? "ASC" : "DESC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlSortByType);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Items sorted by Type (" + order + "):");
            while (rs.next()) {
                String id = rs.getString("id");
                String title = rs.getString("title");
                float price = rs.getFloat("price");
                String type = rs.getString("type");
                String material = rs.getString("material");
                String subtype = rs.getString("subtype");
                int size = rs.getInt("size");

                System.out.println("ID: " + id + ", Title: " + title + ", Price: " + price + ", Type: " + type +
                        ", Material: " + material + ", Subtype: " + subtype + ", Size: " + size);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to sort items by Type.");
        }
    }
    public void sortByPrice(String order) {
        String sqlSortByPrice = "SELECT * FROM item ORDER BY price " + (order.equalsIgnoreCase("ASC") ? "ASC" : "DESC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlSortByPrice);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Items sorted by Price (" + order + "):");
            while (rs.next()) {
                String id = rs.getString("id");
                String title = rs.getString("title");
                float price = rs.getFloat("price");
                String type = rs.getString("type");
                String material = rs.getString("material");
                String subtype = rs.getString("subtype");
                int size = rs.getInt("size");

                System.out.println("ID: " + id + ", Title: " + title + ", Price: " + price + ", Type: " + type +
                        ", Material: " + material + ", Subtype: " + subtype + ", Size: " + size);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to sort items by Price.");
        }
    }

	
    public static void print() {
        String sql = """
            SELECT  
                i.title, 
                i.type,
                i.price, 
                i.material, 
                i.size, 
                i.subtype
            FROM item i;
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                double price = rs.getDouble("price");
                String material = rs.getString("material");
                Integer size = rs.getObject("size", Integer.class); // Null-safe for size
                String subtype = rs.getString("subtype");
                String type = rs.getString("type");

                System.out.print("Type: " + type + ", " + "Product: " + title + ", Price: $" + price + ", Material: " + material);

                if (size != null) {
                    System.out.print(", Size: " + size);
                }
                if (subtype != null) {
                    System.out.print(", Subtype: " + subtype);
                }
                System.out.println(); // New line for each product
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

