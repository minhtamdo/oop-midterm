package ws.cart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import ws.item.Item;
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

public class Cart {
	private ArrayList<Item> itemsInCart = new ArrayList<>();
	public void placeOrder(String email, String address, String phoneNumber) {
	    String sqlGetCartItems = """
	        SELECT c.id_item, c.quantity, c.email, i.price
	        FROM cart c
	        JOIN item i ON c.id_item = i.id
	        WHERE c.email = ?
	    """;

	    String sqlInsertOrder = """
	        INSERT INTO "order" (email, attitude, address, phone_number, time, total)
	        VALUES (?, ?, ?, ?, ?, ?)
	    """;

	    String sqlInsertOrderItem = """
	        INSERT INTO order_item (id_item, id_order, quantity, price)
	        VALUES (?, ?, ?, ?)
	    """;

	    // Bước 1: Lấy thông tin sản phẩm từ giỏ hàng
	    try (Connection conn = DatabaseConnection.getConnection()) {
	        conn.setAutoCommit(false);  // Bắt đầu transaction

	        // Lấy thông tin sản phẩm từ giỏ hàng
	        PreparedStatement stmtGetCartItems = conn.prepareStatement(sqlGetCartItems);
	        stmtGetCartItems.setString(1, email);
	        ResultSet rs = stmtGetCartItems.executeQuery();

	        // Tính tổng giá trị đơn hàng
	        float total = 0;
	        ArrayList<Item> cartItems = new ArrayList<Item>();
	        while (rs.next()) {
	            String idItem = rs.getString("id_item");
	            int quantity = rs.getInt("quantity");
	            float price = rs.getFloat("price");
	            float itemTotal = price * quantity;
	            total += itemTotal;

	            cartItems.add(new Item(idItem, price, quantity));
	        }

	        // Nếu giỏ hàng trống, thông báo lỗi
	        if (cartItems.isEmpty()) {
	            System.out.println("Your cart is empty.");
	            return;
	        }

	        // Bước 2: Tạo đơn hàng mới
	        PreparedStatement stmtInsertOrder = conn.prepareStatement(sqlInsertOrder, Statement.RETURN_GENERATED_KEYS);
	        stmtInsertOrder.setString(1, email);
	        stmtInsertOrder.setString(2, "Pending");  // Trạng thái đơn hàng (Pending khi mới tạo)
	        stmtInsertOrder.setString(3, address);
	        stmtInsertOrder.setString(4, phoneNumber);
	        stmtInsertOrder.setTimestamp(5, new Timestamp(System.currentTimeMillis()));  // Thời gian hiện tại
	        stmtInsertOrder.setFloat(6, total);
	        stmtInsertOrder.executeUpdate();

	        // Lấy ID của đơn hàng vừa tạo
	        ResultSet orderRs = stmtInsertOrder.getGeneratedKeys();
	        int orderId = 0;
	        if (orderRs.next()) {
	            orderId = orderRs.getInt(1);
	        }

	        // Bước 3: Thêm các sản phẩm vào bảng order_item
	        PreparedStatement stmtInsertOrderItem = conn.prepareStatement(sqlInsertOrderItem);
	        for (Item cartItem : cartItems) {
	            stmtInsertOrderItem.setString(1, cartItem.getId());
	            stmtInsertOrderItem.setInt(2, orderId);
	            stmtInsertOrderItem.setInt(3, cartItem.getQuantity());
	            stmtInsertOrderItem.setFloat(4, cartItem.getPrice());
	            stmtInsertOrderItem.addBatch();
	        }
	        stmtInsertOrderItem.executeBatch();

	        // Bước 4: Xóa giỏ hàng của người dùng sau khi đặt hàng
	        String sqlDeleteCart = "DELETE FROM cart WHERE email = ?";
	        PreparedStatement stmtDeleteCart = conn.prepareStatement(sqlDeleteCart);
	        stmtDeleteCart.setString(1, email);
	        stmtDeleteCart.executeUpdate();

	        // Commit transaction
	        conn.commit();
	        System.out.println("Order placed successfully. Your order ID is: " + orderId);
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Failed to place order.");
	    }
	}

	public float totalCost(String email) {
	    float total = 0;

	    String sql = """
	        SELECT c.quantity, i.price
	        FROM cart c
	        JOIN item i ON c.id_item = i.id
	        WHERE c.email = ?
	    """;

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, email); // Đặt email của người dùng

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                int quantity = rs.getInt("quantity");
	                float price = rs.getFloat("price");
	                total += price * quantity; // Tính tổng giá trị
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return total;
	}
	// Can kiem soat xem da co trong list hay chua
	public void addItem(String email, String idItem) {
	    String sqlGetItem = "SELECT * FROM item WHERE id = ?";
	    String sqlCheck = "SELECT quantity FROM cart WHERE email = ? AND id_item = ?";
	    String sqlInsert = "INSERT INTO cart (email, id_item, quantity) VALUES (?, ?, ?)";
	    String sqlUpdate = "UPDATE cart SET quantity = quantity + 1 WHERE email = ? AND id_item = ?";

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
	            // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng chưa
	            try (PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck)) {
	                stmtCheck.setString(1, email);
	                stmtCheck.setString(2, idItem);
	                try (ResultSet rs = stmtCheck.executeQuery()) {
	                    if (rs.next()) {
	                        // Sản phẩm đã tồn tại, tăng số lượng lên
	                        try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
	                            stmtUpdate.setString(1, email);
	                            stmtUpdate.setString(2, idItem);
	                            stmtUpdate.executeUpdate();
	                            System.out.println(item.getTitle() + " quantity has been increased in the Cart.");
	                        }
	                    } else {
	                        // Sản phẩm chưa tồn tại, thêm mới
	                        try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
	                            stmtInsert.setString(1, email);
	                            stmtInsert.setString(2, idItem);
	                            stmtInsert.setInt(3, 1); // Số lượng mặc định là 1
	                            stmtInsert.executeUpdate();
	                            System.out.println(item.getTitle() + " has been added to the Cart.");
	                        }
	                    }
	                }
	            }
	        } else {
	            System.out.println("Item with ID " + idItem + " not found in the database.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("Failed to add item with ID " + idItem + " to the Cart.");
	    }
	}
	public void changeQuantity(String email, String idItem, int quantity) {
	    // Kiểm tra xem quantity có hợp lệ không (phải > 0)
	    if (quantity <= 0) {
	        System.out.println("Quantity must be greater than 0.");
	        return;  // Dừng hàm nếu quantity không hợp lệ
	    }

	    String sqlUpdate = "UPDATE cart SET quantity = ? WHERE email = ? AND id_item = ?";

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {

	        // Set các giá trị vào câu lệnh PreparedStatement
	        stmt.setInt(1, quantity);
	        stmt.setString(2, email);
	        stmt.setString(3, idItem);

	        // Thực thi câu lệnh UPDATE và trả về số dòng bị ảnh hưởng
	        int rowsAffected = stmt.executeUpdate();

	        if (rowsAffected > 0) {
	            System.out.println("Quantity of item with ID " + idItem + " has been updated to " + quantity + " in the cart for email " + email);
	        } else {
	            System.out.println("Item with ID " + idItem + " not found in the cart for email " + email);
	        }
	    } catch (SQLException e) {
	        // In ra lỗi nếu có
	        e.printStackTrace();
	        System.out.println("Failed to update quantity of item with ID " + idItem + " in the cart for email " + email);
	    }
	}

    public void removeItem(String email, String idItem) {
        String sqlDelete = "DELETE FROM cart WHERE email = ? AND id_item = ?";

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
            SELECT c.id_item, i.title, i.price, i.type, i.material, i.subtype, i.size, c.quantity
            FROM cart c
            JOIN item i ON c.id_item = i.id
            WHERE c.email = ? AND i.title ILIKE ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlSearchByTitle)) {

            stmt.setString(1, email);
            stmt.setString(2, "%" + title + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Items in cart matching title: " + title);
                while (rs.next()) {
                    String idItem = rs.getString("id_item");
                    String itemTitle = rs.getString("title");
                    float price = rs.getFloat("price");
                    String type = rs.getString("type");
                    String material = rs.getString("material");
                    String subtype = rs.getString("subtype");
                    int size = rs.getInt("size");
                    int quantity = rs.getInt("quantity");

                    System.out.printf("ID: %s, Title: %s, Price: %.2f, Type: %s, Material: %s, Subtype: %s, Size: %d, Quantity: %d%n",
                            idItem, itemTitle, price, type, material, subtype != null ? subtype : "N/A", size > 0 ? size : 0, quantity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to search items by Title in Cart.");
        }
    }
    public void searchById(String email, String idPart) {
        String sqlSearchById = """
            SELECT c.id_item, i.title, i.price, i.type, i.material, i.subtype, i.size, c.quantity
            FROM cart c
            JOIN item i ON c.id_item = i.id
            WHERE c.email = ? AND c.id_item LIKE ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlSearchById)) {

            stmt.setString(1, email);
            stmt.setString(2, "%" + idPart + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Items in cart matching ID: " + idPart);
                while (rs.next()) {
                    String idItem = rs.getString("id_item");
                    String title = rs.getString("title");
                    float price = rs.getFloat("price");
                    String type = rs.getString("type");
                    String material = rs.getString("material");
                    String subtype = rs.getString("subtype");
                    int size = rs.getInt("size");
                    int quantity = rs.getInt("quantity");

                    System.out.printf("ID: %s, Title: %s, Price: %.2f, Type: %s, Material: %s, Subtype: %s, Size: %d, Quantity: %d%n",
                            idItem, title, price, type, material, subtype != null ? subtype : "N/A", size > 0 ? size : 0, quantity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to search item by ID in Cart.");
        }
    }

    public void searchByType(String email, String type) {
        String sqlSearchByType = """
            SELECT c.id_item, i.title, i.price, i.type, i.material, i.subtype, i.size, c.quantity
            FROM cart c
            JOIN item i ON c.id_item = i.id
            WHERE c.email = ? AND i.type ILIKE ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlSearchByType)) {

            stmt.setString(1, email);
            stmt.setString(2, "%" + type + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Items in cart matching type: " + type);
                while (rs.next()) {
                    String idItem = rs.getString("id_item");
                    String title = rs.getString("title");
                    float price = rs.getFloat("price");
                    String itemType = rs.getString("type");
                    String material = rs.getString("material");
                    String subtype = rs.getString("subtype");
                    int size = rs.getInt("size");
                    int quantity = rs.getInt("quantity");

                    System.out.printf("ID: %s, Title: %s, Price: %.2f, Type: %s, Material: %s, Subtype: %s, Size: %d, Quantity: %d%n",
                            idItem, title, price, itemType, material, subtype != null ? subtype : "N/A", size > 0 ? size : 0, quantity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to search items by Type in Cart.");
        }
    }

    public void sortCartByProductName(String email, String order) {
        String sqlSortByName = """
            SELECT c.id_item, i.title, i.price, i.type, i.material, i.subtype, i.size, c.quantity, (i.price * c.quantity) AS totalCost
            FROM cart c
            JOIN item i ON c.id_item = i.id
            WHERE c.email = ?
            ORDER BY i.title """ + (order.equalsIgnoreCase("ASC") ? " ASC" : " DESC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlSortByName)) {

            stmt.setString(1, email);

            float totalCartCost = 0;

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Cart sorted by Product Name (" + order + "):");
                while (rs.next()) {
                    String id = rs.getString("id_item");
                    String title = rs.getString("title");
                    float price = rs.getFloat("price");
                    String type = rs.getString("type");
                    String material = rs.getString("material");
                    String subtype = rs.getString("subtype");
                    int size = rs.getInt("size");
                    int quantity = rs.getInt("quantity");
                    float totalCost = rs.getFloat("totalCost");

                    totalCartCost += totalCost;

                    System.out.printf("ID: %s, Title: %s, Price: %.2f, Type: %s, Material: %s, Subtype: %s, Size: %d, Quantity: %d, Total Cost: %.2f%n",
                            id, title, price, type, material, subtype != null ? subtype : "N/A",
                            size > 0 ? size : 0, quantity, totalCost);
                }
                System.out.printf("\nTotal Cost of Cart: %.2f%n", totalCartCost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to sort cart by Product Name.");
        }
    }

    public void sortCartByType(String email, String order) {
        String sqlSortByType = """
            SELECT c.id_item, i.title, i.price, i.type, i.material, i.subtype, i.size, c.quantity, (i.price * c.quantity) AS totalCost
            FROM cart c
            JOIN item i ON c.id_item = i.id
            WHERE c.email = ?
            ORDER BY i.type """ + (order.equalsIgnoreCase("ASC") ? " ASC" : " DESC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlSortByType)) {

            stmt.setString(1, email);

            float totalCartCost = 0;

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Cart sorted by Type (" + order + "):");
                while (rs.next()) {
                    String id = rs.getString("id_item");
                    String title = rs.getString("title");
                    float price = rs.getFloat("price");
                    String type = rs.getString("type");
                    String material = rs.getString("material");
                    String subtype = rs.getString("subtype");
                    int size = rs.getInt("size");
                    int quantity = rs.getInt("quantity");
                    float totalCost = rs.getFloat("totalCost");

                    totalCartCost += totalCost;

                    System.out.printf("ID: %s, Title: %s, Price: %.2f, Type: %s, Material: %s, Subtype: %s, Size: %d, Quantity: %d, Total Cost: %.2f%n",
                            id, title, price, type, material, subtype != null ? subtype : "N/A",
                            size > 0 ? size : 0, quantity, totalCost);
                }
                System.out.printf("\nTotal Cost of Cart: %.2f%n", totalCartCost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to sort cart by Type.");
        }
    }
    public void sortCartByPrice(String email, String order) {
        String sqlSortByPrice = """
            SELECT c.id_item, i.title, i.price, i.type, i.material, i.subtype, i.size, c.quantity, (i.price * c.quantity) AS totalCost
            FROM cart c
            JOIN item i ON c.id_item = i.id
            WHERE c.email = ?
            ORDER BY i.price """ + (order.equalsIgnoreCase("ASC") ? " ASC" : " DESC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlSortByPrice)) {

            stmt.setString(1, email);

            float totalCartCost = 0;

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Cart sorted by Price (" + order + "):");
                while (rs.next()) {
                    String id = rs.getString("id_item");
                    String title = rs.getString("title");
                    float price = rs.getFloat("price");
                    String type = rs.getString("type");
                    String material = rs.getString("material");
                    String subtype = rs.getString("subtype");
                    int size = rs.getInt("size");
                    int quantity = rs.getInt("quantity");
                    float totalCost = rs.getFloat("totalCost");

                    totalCartCost += totalCost;

                    System.out.printf("ID: %s, Title: %s, Price: %.2f, Type: %s, Material: %s, Subtype: %s, Size: %d, Quantity: %d, Total Cost: %.2f%n",
                            id, title, price, type, material, subtype != null ? subtype : "N/A",
                            size > 0 ? size : 0, quantity, totalCost);
                }
                System.out.printf("\nTotal Cost of Cart: %.2f%n", totalCartCost);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to sort cart by Price.");
        }
    }

    public static void print(String email) {
        String sql = """
                SELECT 
                    c.email, 
                    c.quantity,
                    i.title, 
                    i.type,
                    i.price, 
                    i.material, 
                    i.size, 
                    i.subtype
                FROM cart c
                JOIN item i ON c.id_item = i.id
                WHERE c.email = ?
            """;

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();

                System.out.println("Cart for email: " + email);
                float totalCost =0;
                while (rs.next()) {
                    String title = rs.getString("title");
                    double price = rs.getDouble("price");
                    String material = rs.getString("material");
                    Integer size = rs.getObject("size", Integer.class); // Null-safe for size
                    String subtype = rs.getString("subtype");
                    String type = rs.getString("type");
                    int quantity = rs.getInt("quantity");

                    System.out.print("Type: " + type + ", " + "Product: " + title + ", Price: $" + price + ", Material: " + material + ", Quantity: " + quantity);
                    totalCost += price*quantity;
                    if (size != null) {
                        System.out.print(", Size: " + size);
                    }
                    if (subtype != null) {
                        System.out.print(", Subtype: " + subtype);
                    }
                    System.out.println(); // New line for each product
                }
                System.out.println(totalCost);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
