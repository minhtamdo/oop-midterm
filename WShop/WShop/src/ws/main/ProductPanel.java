package ws.main;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ws.person.Person;

public class ProductPanel extends JPanel {
    private String productId;
    private String title;
    private float price;
    private String material;
    private String subtype;
    private Integer size;
    private String type;
    private MainMenu mainMenu;
    private CartPanel cartPanel;
    private FavouritePanel favouritePanel;

    // Constructor cho ProductPanel trong Store
    public ProductPanel(String productId, String title, float price, String material, String subtype, Integer size, String type, MainMenu mainMenu) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.material = material;
        this.subtype = subtype;
        this.size = size;
        this.type = type;
        this.mainMenu = mainMenu;  // Gán MainMenu

        // Layout cho JPanel
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Hiển thị type và title
        JLabel typeLabel = new JLabel(type + ": " + title);
        typeLabel.setFont(new Font(typeLabel.getFont().getName(), Font.PLAIN, 18));
        typeLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Các thông tin khác
        JLabel idLabel = new JLabel("ID: " + productId);
        idLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel("Price: $" + price);
        priceLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel materialLabel = new JLabel("Material: " + material);
        materialLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtypeLabel = new JLabel("Subtype: " + (subtype != null ? subtype : "N/A"));
        subtypeLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sizeLabel = new JLabel("Size: " + (size != null ? size : "N/A"));
        sizeLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Nút "Add to Cart"
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.setAlignmentX(CENTER_ALIGNMENT);
        addToCartButton.addActionListener(e -> {
            Person nowPerson = mainMenu.getNowPerson();
            if (nowPerson == null) {
                JOptionPane.showMessageDialog(this, "You must be logged in to add to cart.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Gọi hàm addToCart với itemId và email của người dùng
                addToCart(productId, nowPerson.getEmail());
            }
        });

        // Nút "Add to Favourite"
        JButton addToFavouriteButton = new JButton("Add to Favourite");
        addToFavouriteButton.setAlignmentX(CENTER_ALIGNMENT);

        addToFavouriteButton.addActionListener(e -> {
            Person nowPerson = mainMenu.getNowPerson();
            if (nowPerson == null) {
                JOptionPane.showMessageDialog(this, "You must be logged in to add to favourites.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Gọi hàm addToFavourite với itemId và email của người dùng
                addToFavourite(productId, nowPerson.getEmail());
            }
        });

        // Tạo container cho các nút
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonContainer.add(addToCartButton);
        buttonContainer.add(addToFavouriteButton);

        // Thêm các thành phần vào JPanel
        this.add(Box.createVerticalGlue());
        this.add(typeLabel);
        this.add(idLabel);
        this.add(priceLabel);
        this.add(materialLabel);
        this.add(subtypeLabel);
        this.add(sizeLabel);
        this.add(Box.createVerticalGlue());
        this.add(buttonContainer);
    }

    // Constructor cho ProductPanel trong Favourite
    public ProductPanel(String productId, String title, float price, String material, String subtype, Integer size, String type, FavouritePanel favouritePanel) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.material = material;
        this.subtype = subtype;
        this.size = size;
        this.type = type;
        this.favouritePanel = favouritePanel;  // Gán MainMenu

        // Layout cho JPanel
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Hiển thị type và title
        JLabel typeLabel = new JLabel(type + ": " + title);
        typeLabel.setFont(new Font(typeLabel.getFont().getName(), Font.PLAIN, 18));
        typeLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Các thông tin khác
        JLabel idLabel = new JLabel("ID: " + productId);
        idLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel("Price: $" + price);
        priceLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel materialLabel = new JLabel("Material: " + material);
        materialLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtypeLabel = new JLabel("Subtype: " + (subtype != null ? subtype : "N/A"));
        subtypeLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sizeLabel = new JLabel("Size: " + (size != null ? size : "N/A"));
        sizeLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Nút "Add to Cart"
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.setAlignmentX(CENTER_ALIGNMENT);
        addToCartButton.addActionListener(e -> {
            Person nowPerson = mainMenu.getNowPerson();
            if (nowPerson == null) {
                JOptionPane.showMessageDialog(this, "You must be logged in to add to cart.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Gọi hàm addToCart với itemId và email của người dùng
                addToCart(productId, nowPerson.getEmail());
            }
        });

        // Nút "Add to Favourite"
        JButton addToFavouriteButton = new JButton("Add to Favourite");
        addToFavouriteButton.setAlignmentX(CENTER_ALIGNMENT);

        addToFavouriteButton.addActionListener(e -> {
            Person nowPerson = mainMenu.getNowPerson();
            if (nowPerson == null) {
                JOptionPane.showMessageDialog(this, "You must be logged in to add to favourites.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Gọi hàm addToFavourite với itemId và email của người dùng
                addToFavourite(productId, nowPerson.getEmail());
            }
        });

        // Tạo container cho các nút
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonContainer.add(addToCartButton);
        buttonContainer.add(addToFavouriteButton);

        // Thêm các thành phần vào JPanel
        this.add(Box.createVerticalGlue());
        this.add(typeLabel);
        this.add(idLabel);
        this.add(priceLabel);
        this.add(materialLabel);
        this.add(subtypeLabel);
        this.add(sizeLabel);
        this.add(Box.createVerticalGlue());
        this.add(buttonContainer);
    }
 
    // Constructor cho ProductPanel trong Cart
    public ProductPanel(String productId, String title, float price, String material, String subtype, Integer size, String type, CartPanel cartPanel) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.material = material;
        this.subtype = subtype;
        this.size = size;
        this.type = type;
        this.cartPanel = cartPanel;  // Gán MainMenu

        // Layout cho JPanel
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Hiển thị type và title
        JLabel typeLabel = new JLabel(type + ": " + title);
        typeLabel.setFont(new Font(typeLabel.getFont().getName(), Font.PLAIN, 18));
        typeLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Các thông tin khác
        JLabel idLabel = new JLabel("ID: " + productId);
        idLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel("Price: $" + price);
        priceLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel materialLabel = new JLabel("Material: " + material);
        materialLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel subtypeLabel = new JLabel("Subtype: " + (subtype != null ? subtype : "N/A"));
        subtypeLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sizeLabel = new JLabel("Size: " + (size != null ? size : "N/A"));
        sizeLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Nút "Add to Cart"
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.setAlignmentX(CENTER_ALIGNMENT);
        addToCartButton.addActionListener(e -> {
            Person nowPerson = mainMenu.getNowPerson();
            if (nowPerson == null) {
                JOptionPane.showMessageDialog(this, "You must be logged in to add to cart.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Gọi hàm addToCart với itemId và email của người dùng
                addToCart(productId, nowPerson.getEmail());
            }
        });

        // Nút "Add to Favourite"
        JButton addToFavouriteButton = new JButton("Add to Favourite");
        addToFavouriteButton.setAlignmentX(CENTER_ALIGNMENT);

        addToFavouriteButton.addActionListener(e -> {
            Person nowPerson = mainMenu.getNowPerson();
            if (nowPerson == null) {
                JOptionPane.showMessageDialog(this, "You must be logged in to add to favourites.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Gọi hàm addToFavourite với itemId và email của người dùng
                addToFavourite(productId, nowPerson.getEmail());
            }
        });

        // Tạo container cho các nút
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonContainer.add(addToCartButton);
        buttonContainer.add(addToFavouriteButton);

        // Thêm các thành phần vào JPanel
        this.add(Box.createVerticalGlue());
        this.add(typeLabel);
        this.add(idLabel);
        this.add(priceLabel);
        this.add(materialLabel);
        this.add(subtypeLabel);
        this.add(sizeLabel);
        this.add(Box.createVerticalGlue());
        this.add(buttonContainer);
    }

    // Phương thức tạo ProductPanel từ ResultSet cho Store
    public static ProductPanel createFromResultSet(ResultSet resultSet, MainMenu mainMenu) throws SQLException {
        String productId = resultSet.getString("id");
        String title = resultSet.getString("title");
        float price = resultSet.getFloat("price");
        String material = resultSet.getString("material");
        String subtype = resultSet.getString("subtype");
        Integer size = resultSet.getObject("size") != null ? resultSet.getInt("size") : null;
        String type = resultSet.getString("type");

        return new ProductPanel(productId, title, price, material, subtype, size, type, mainMenu);  // Thêm mainMenu vào constructor
    }
    
    // Phương thức tạo ProductPanel từ ResultSet cho Favourite
    public static ProductPanel createFromResultSet(ResultSet resultSet, FavouritePanel favouritePanel) throws SQLException {
        String productId = resultSet.getString("id");
        String title = resultSet.getString("title");
        float price = resultSet.getFloat("price");
        String material = resultSet.getString("material");
        String subtype = resultSet.getString("subtype");
        Integer size = resultSet.getObject("size") != null ? resultSet.getInt("size") : null;
        String type = resultSet.getString("type");

        return new ProductPanel(productId, title, price, material, subtype, size, type, favouritePanel);  // Thêm mainMenu vào constructor
    }

    // Phương thức tạo ProductPanel từ ResultSet cho Cart
    public static ProductPanel createFromResultSet(ResultSet resultSet, CartPanel cartPanel) throws SQLException {
        String productId = resultSet.getString("id");
        String title = resultSet.getString("title");
        float price = resultSet.getFloat("price");
        String material = resultSet.getString("material");
        String subtype = resultSet.getString("subtype");
        Integer size = resultSet.getObject("size") != null ? resultSet.getInt("size") : null;
        String type = resultSet.getString("type");

        return new ProductPanel(productId, title, price, material, subtype, size, type, cartPanel);  // Thêm mainMenu vào constructor
    }

    // Phương thức thêm vào giỏ hàng
    private void addToCart(String itemId, String userEmail) {
        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        String checkQuery = "SELECT * FROM cart WHERE email = ? AND id_item = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {

            checkStmt.setString(1, userEmail);
            checkStmt.setString(2, itemId);

            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Nếu đã có, tăng số lượng lên 1
                int currentQuantity = rs.getInt("quantity");
                String updateQuery = "UPDATE cart SET quantity = ? WHERE email = ? AND id_item = ?";

                try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                    updateStmt.setInt(1, currentQuantity + 1);  // Cộng thêm 1 vào số lượng
                    updateStmt.setString(2, userEmail);
                    updateStmt.setString(3, itemId);

                    int rowsAffected = updateStmt.executeUpdate();

                    if (rowsAffected > 0) {
                    	String string = title +" quantity increased to " + (currentQuantity + 1) + " in the cart.";
                        JOptionPane.showMessageDialog(this, string, "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to update item quantity in the cart.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                // Nếu chưa có, thêm sản phẩm vào giỏ hàng với số lượng là 1
                String insertQuery = "INSERT INTO cart (email, id_item, quantity) VALUES (?, ?, 1)";

                try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                    insertStmt.setString(1, userEmail);
                    insertStmt.setString(2, itemId);

                    int rowsAffected = insertStmt.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, title + " has been added to the cart!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to add item to the cart.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while adding to the cart.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Phương thức thêm vào danh sách yêu thích
    private void addToFavourite(String itemId, String userEmail) {
        // Kiểm tra xem sản phẩm đã có trong favourites chưa
        String checkQuery = "SELECT * FROM Favourite WHERE email = ? AND id_item = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {

            checkStmt.setString(1, userEmail);
            checkStmt.setString(2, itemId);

            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Nếu đã có, thông báo cho người dùng
                JOptionPane.showMessageDialog(this, "This item is already in your favourites.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;  // Không làm gì thêm nếu sản phẩm đã có
            }

            // Nếu chưa có, tiến hành thêm sản phẩm vào favourites
            String insertQuery = "INSERT INTO Favourite (email, id_item) VALUES (?, ?)";

            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setString(1, userEmail);
                insertStmt.setString(2, itemId);

                int rowsAffected = insertStmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Item added to favourites!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add item to favourites.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while adding to favourites.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
