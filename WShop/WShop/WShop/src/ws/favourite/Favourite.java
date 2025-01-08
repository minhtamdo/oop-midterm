package ws.favourite;

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
import ws.item.accessories.Bag;
import ws.item.accessories.Glasses;
import ws.item.accessories.Hat;
import ws.item.footwear.Shoe;
import ws.item.footwear.Sock;
import ws.item.outerwear.Coat;
import ws.item.pants.Jean;
import ws.item.pants.Jogger;
import ws.item.pants.Trouser;
import ws.item.top.Croptop;
import ws.item.top.Hoodie;
import ws.item.top.Shirt;
import ws.item.top.Sweater;
import ws.item.top.Tshirt;
import ws.main.DatabaseConnection;

public class Favourite {
	public void addItem(String email, String idItem) {
	    String sqlGetItem = "SELECT * FROM item WHERE id = ?";
	    String sqlCheck = "SELECT 1 FROM favourite WHERE email = ? AND id_item = ?";
	    String sqlInsert = "INSERT INTO favourite (email, id_item) VALUES (?, ?)";

	    try (Connection conn = DatabaseConnection.getConnection()) {
	        // Tìm thông tin sản phẩm từ bảng item
	        Item item = null;
	        try (PreparedStatement stmtGetItem = conn.prepareStatement(sqlGetItem)) {
	            stmtGetItem.setString(1, idItem);
	            try (ResultSet rs = stmtGetItem.executeQuery()) {
	                if (rs.next()) {
	                    String type = rs.getString("type");
	                    String title = rs.getString("title");
	                    float price = rs.getFloat("price");
	                    String material = rs.getString("material");
	                    int size = rs.getInt("size");
	                    String subtype = rs.getString("subtype");

	                    // Tạo đối tượng item phù hợp với type
	                    switch (type) {
	                        case "Tshirt":
	                            item = new Tshirt(idItem, title, price, type, material, subtype, size);
	                            break;
	                        case "Bag":
	                            item = new Bag(idItem, title, price, type, material, subtype, size);
	                            break;
	                        case "Glasses":
	                            item = new Glasses(idItem, title, price, type, material, subtype);
	                            break;
	                        case "Hat":
	                            item = new Hat(idItem, title, price, type, material, subtype);
	                            break;
	                        case "Shoe":
	                            item = new Shoe(idItem, title, price, type, material, subtype, size);
	                            break;
	                        case "Sock":
	                            item = new Sock(idItem, title, price, type, material, subtype);
	                            break;
	                        case "Coat":
	                            item = new Coat(idItem, title, price, type, material, subtype, size);
	                            break;
	                        case "Jean":
	                            item = new Jean(idItem, title, price, type, material, subtype, size);
	                            break;
	                        case "Trouser":
	                            item = new Trouser(idItem, title, price, type, material, subtype, size);
	                            break;
	                        case "Jogger":
	                            item = new Jogger(idItem, title, price, type, material, size);
	                            break;
	                        case "Hoodie":
	                            item = new Hoodie(idItem, title, price, type, material, subtype, size);
	                            break;
	                        case "Croptop":
	                            item = new Croptop(idItem, title, price, type, material, subtype, size);
	                            break;
	                        case "Shirt":
	                            item = new Shirt(idItem, title, price, type, material, subtype, size);
	                            break;
	                        case "Sweater":
	                            item = new Sweater(idItem, title, price, type, material, size);
	                            break;
	                        default:
	                            System.out.println("Unknown item type: " + type);
	                    }
	                }
	            }
	        }

	        if (item != null) {
	            // Kiểm tra xem sản phẩm đã có trong danh sách yêu thích chưa
	            try (PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck)) {
	                stmtCheck.setString(1, email);
	                stmtCheck.setString(2, idItem);
	                try (ResultSet rs = stmtCheck.executeQuery()) {
	                    if (rs.next()) {
	                        System.out.println(item.getTitle() + " is already in the Favourite list.");
	                    } else {
	                        // Sản phẩm chưa có trong danh sách yêu thích, thêm mới
	                        try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
	                            stmtInsert.setString(1, email);
	                            stmtInsert.setString(2, idItem);
	                            stmtInsert.executeUpdate();
	                            System.out.println(item.getTitle() + " has been added to the Favourite.");
	                        }
	                    }
	                }
	            }
	        } else {
	            System.out.println("Item with ID " + idItem + " not found in the database.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("Failed to add item with ID " + idItem + " to the Favourite.");
	    }
	}

	public void removeItem(String email, String idItem) {
        String sqlDelete = "DELETE FROM favourite WHERE email = ? AND id_item = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlDelete)) {

            // Set giá trị vào câu lệnh PreparedStatement
            stmt.setString(1, email);
            stmt.setString(2, idItem);

            // Thực thi câu lệnh DELETE và trả về số dòng bị ảnh hưởng
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Item with ID " + idItem + " has been removed from the cart of email " + email);
            } else {
                System.out.println("Item with ID " + idItem + " not found in the cart for email " + email);
            }
        } catch (SQLException e) {
            // In ra lỗi nếu có
            e.printStackTrace();
            System.out.println("Failed to remove item with ID " + idItem + " from the cart of email " + email);
        }
    }

	public void searchByTitle(String email, String title) {
	    String sqlSearchByTitle = """
	        SELECT f.id_item, i.title, i.price, i.type, i.material, i.subtype, i.size
	        FROM favourite f
	        JOIN item i ON f.id_item = i.id
	        WHERE f.email = ? AND i.title ILIKE ?
	    """;

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sqlSearchByTitle)) {

	        stmt.setString(1, email);
	        stmt.setString(2, "%" + title + "%");

	        try (ResultSet rs = stmt.executeQuery()) {
	            System.out.println("Items in favourites matching title: " + title);
	            while (rs.next()) {
	                String idItem = rs.getString("id_item");
	                String itemTitle = rs.getString("title");
	                float price = rs.getFloat("price");
	                String type = rs.getString("type");
	                String material = rs.getString("material");
	                String subtype = rs.getString("subtype");
	                int size = rs.getInt("size");

	                System.out.printf("ID: %s, Title: %s, Price: %.2f, Type: %s, Material: %s, Subtype: %s, Size: %d%n",
	                        idItem, itemTitle, price, type, material, subtype != null ? subtype : "N/A", size > 0 ? size : 0);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Failed to search items by Title in Favourites.");
	    }
	}
	public void searchById(String email, String idPart) {
	    String sqlSearchById = """
	        SELECT f.id_item, i.title, i.price, i.type, i.material, i.subtype, i.size
	        FROM favourite f
	        JOIN item i ON f.id_item = i.id
	        WHERE f.email = ? AND f.id_item LIKE ?
	    """;

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sqlSearchById)) {

	        stmt.setString(1, email);
	        stmt.setString(2, "%" + idPart + "%");

	        try (ResultSet rs = stmt.executeQuery()) {
	            System.out.println("Items in favourites matching ID: " + idPart);
	            while (rs.next()) {
	                String idItem = rs.getString("id_item");
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
	        System.out.println("Failed to search item by ID in Favourites.");
	    }
	}

	public void searchByType(String email, String type) {
	    String sqlSearchByType = """
	        SELECT f.id_item, i.title, i.price, i.type, i.material, i.subtype, i.size
	        FROM favourite f
	        JOIN item i ON f.id_item = i.id
	        WHERE f.email = ? AND i.type ILIKE ?
	    """;

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sqlSearchByType)) {

	        stmt.setString(1, email);
	        stmt.setString(2, "%" + type + "%");

	        try (ResultSet rs = stmt.executeQuery()) {
	            System.out.println("Items in favourites matching type: " + type);
	            while (rs.next()) {
	                String idItem = rs.getString("id_item");
	                String title = rs.getString("title");
	                float price = rs.getFloat("price");
	                String itemType = rs.getString("type");
	                String material = rs.getString("material");
	                String subtype = rs.getString("subtype");
	                int size = rs.getInt("size");

	                System.out.printf("ID: %s, Title: %s, Price: %.2f, Type: %s, Material: %s, Subtype: %s, Size: %d%n",
	                        idItem, title, price, itemType, material, subtype != null ? subtype : "N/A", size > 0 ? size : 0);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Failed to search items by Type in Favourites.");
	    }
	}
    public void sortFavouriteByTitle(String email, String order) {
        String sqlSortByTitle = """
            SELECT f.id_item, i.title, i.price, i.type, i.material, i.subtype, i.size
            FROM favourite f
            JOIN item i ON f.id_item = i.id
            WHERE f.email = ?
            ORDER BY i.title """ + (order.equalsIgnoreCase("ASC") ? " ASC" : " DESC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlSortByTitle)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Favourite items sorted by Title (" + order + "):");
                while (rs.next()) {
                    String id = rs.getString("id_item");
                    String title = rs.getString("title");
                    float price = rs.getFloat("price");
                    String type = rs.getString("type");
                    String material = rs.getString("material");
                    String subtype = rs.getString("subtype");
                    int size = rs.getInt("size");

                    System.out.printf("ID: %s, Title: %s, Price: %.2f, Type: %s, Material: %s, Subtype: %s, Size: %d%n",
                            id, title, price, type, material, subtype != null ? subtype : "N/A", size > 0 ? size : 0);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to sort favourites by Title.");
        }
    }
    public void sortFavouriteByPrice(String email, String order) {
        String sqlSortByPrice = """
            SELECT f.id_item, i.title, i.price, i.type, i.material, i.subtype, i.size
            FROM favourite f
            JOIN item i ON f.id_item = i.id
            WHERE f.email = ?
            ORDER BY i.price """ + (order.equalsIgnoreCase("ASC") ? " ASC" : " DESC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlSortByPrice)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Favourite items sorted by Price (" + order + "):");
                while (rs.next()) {
                    String id = rs.getString("id_item");
                    String title = rs.getString("title");
                    float price = rs.getFloat("price");
                    String type = rs.getString("type");
                    String material = rs.getString("material");
                    String subtype = rs.getString("subtype");
                    int size = rs.getInt("size");

                    System.out.printf("ID: %s, Title: %s, Price: %.2f, Type: %s, Material: %s, Subtype: %s, Size: %d%n",
                            id, title, price, type, material, subtype != null ? subtype : "N/A", size > 0 ? size : 0);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to sort favourites by Price.");
        }
    }
    public void sortFavouriteByType(String email, String order) {
        String sqlSortByType = """
            SELECT f.id_item, i.title, i.price, i.type, i.material, i.subtype, i.size
            FROM favourite f
            JOIN item i ON f.id_item = i.id
            WHERE f.email = ?
            ORDER BY i.type """ + (order.equalsIgnoreCase("ASC") ? " ASC" : " DESC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlSortByType)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Favourite items sorted by Type (" + order + "):");
                while (rs.next()) {
                    String id = rs.getString("id_item");
                    String title = rs.getString("title");
                    float price = rs.getFloat("price");
                    String type = rs.getString("type");
                    String material = rs.getString("material");
                    String subtype = rs.getString("subtype");
                    int size = rs.getInt("size");

                    System.out.printf("ID: %s, Title: %s, Price: %.2f, Type: %s, Material: %s, Subtype: %s, Size: %d%n",
                            id, title, price, type, material, subtype != null ? subtype : "N/A", size > 0 ? size : 0);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to sort favourites by Type.");
        }
    }

    public static void print(String email) {
        String sql = """
            SELECT 
                f.email, 
                i.title, 
                i.type,
                i.price, 
                i.material, 
                i.size, 
                i.subtype
            FROM favourite f
            JOIN item i ON f.id_item = i.id
            WHERE f.email = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            System.out.println("Favourites for email: " + email);
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

