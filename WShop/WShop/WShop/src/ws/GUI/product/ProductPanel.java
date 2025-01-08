package ws.GUI.product;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import ws.GUI.screen.CartScreen;
import ws.GUI.screen.FavouriteScreen;
import ws.GUI.screen.ManagerStoreScreen;
import ws.GUI.screen.StoreScreen;
import ws.main.DatabaseConnection;
import ws.person.Person;

public class ProductPanel extends JPanel {
    private String productId;
    private String title;
    private float price;
    private String material;
    private String subtype;
    private Integer size;
    private String type;
    private int quantity;
    private ManagerStoreScreen managerStoreScreen;
    private StoreScreen storeScreen;
    private CartScreen cartScreen;
    private FavouriteScreen favouriteScreen;

    // Constructor cho ProductPanel trong Store
    public ProductPanel(String productId, String title, float price, String material, String subtype, Integer size, String type, StoreScreen storeScreen) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.material = material;
        this.subtype = subtype;
        this.size = size;
        this.type = type;
        this.storeScreen = storeScreen;  // Gán MainMenu

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
            Person nowPerson = storeScreen.getNowPerson();
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
            Person nowPerson = storeScreen.getNowPerson();
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
    public ProductPanel(String productId, String title, float price, String material, String subtype, Integer size, String type, FavouriteScreen favouriteScreen) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.material = material;
        this.subtype = subtype;
        this.size = size;
        this.type = type;
        this.favouriteScreen = favouriteScreen;  // Gán FavouriteScreen

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
            Person nowPerson = favouriteScreen.getNowPerson();
            if (nowPerson == null) {
                JOptionPane.showMessageDialog(this, "You must be logged in to add to cart.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Gọi hàm addToCart với itemId và email của người dùng
                addToCart(productId, nowPerson.getEmail());
            }
        });

        // Nút "Remove from Favourites"
        JButton removeFromFavouritesButton = new JButton("Remove");
        removeFromFavouritesButton.setAlignmentX(CENTER_ALIGNMENT);
        removeFromFavouritesButton.addActionListener(e -> {
            Person nowPerson = favouriteScreen.getNowPerson();
            if (nowPerson == null) {
                JOptionPane.showMessageDialog(this, "You must be logged in to remove from favourites.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // Gọi hàm removeFromFavourites với itemId và email của người dùng
                removeProductFavourite(productId, nowPerson.getEmail());
                favouriteScreen.loadProductList();
                favouriteScreen.displayProducts(favouriteScreen.productList);
            }
        });

        // Tạo container cho các nút
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonContainer.add(addToCartButton);
        buttonContainer.add(removeFromFavouritesButton);  // Thêm nút xóa vào container

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
    public ProductPanel(String productId, String title, float price, String material, String subtype, Integer size, String type, int quantity, CartScreen cartScreen) {
	    this.productId = productId;
	    this.title = title;
	    this.price = price;
	    this.material = material;
	    this.subtype = subtype;
	    this.size = size;
	    this.type = type;
	    this.quantity = quantity;
	    this.cartScreen = cartScreen;  // Gán CartScreen
	
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
	
	    JLabel quantityLabel = new JLabel("Quantity: " + quantity);
	    quantityLabel.setAlignmentX(CENTER_ALIGNMENT);

	    // Nút "Add to Favourite"
	    JButton addToFavouriteButton = new JButton("Add to Favourite");
	    addToFavouriteButton.setAlignmentX(CENTER_ALIGNMENT);
	
	    addToFavouriteButton.addActionListener(e -> {
	        Person nowPerson = cartScreen.getNowPerson();
	        if (nowPerson == null) {
	            JOptionPane.showMessageDialog(this, "You must be logged in to add to favourites.", "Error", JOptionPane.ERROR_MESSAGE);
	        } else {
	            // Gọi hàm addToFavourite với itemId và email của người dùng
	            addToFavourite(productId, nowPerson.getEmail());
	        }
	    });
	
	    // Nút "Remove from Cart"
	    JButton removeFromCartButton = new JButton("Remove");
	    removeFromCartButton.setAlignmentX(CENTER_ALIGNMENT);
	    removeFromCartButton.addActionListener(e -> {
	        Person nowPerson = cartScreen.getNowPerson();
	        if (nowPerson == null) {
	            JOptionPane.showMessageDialog(this, "You must be logged in to remove from cart.", "Error", JOptionPane.ERROR_MESSAGE);
	        } else {
	            // Gọi hàm removeFromCart với itemId và email của người dùng
	            removeProductFromCart(productId, nowPerson.getEmail());
	            cartScreen.loadProductList();
	            cartScreen.displayProducts(cartScreen.productList);
	        }
	    });
	    
	    // Nút "Add to Ind Dec"
	    JButton plusButton = new JButton(" + ");
	    plusButton.setAlignmentX(CENTER_ALIGNMENT);
	    plusButton.addActionListener(e -> {
	    	Person nowPerson = cartScreen.getNowPerson();
	        if (nowPerson == null) {
	            JOptionPane.showMessageDialog(this, "You must be logged in to do this.", "Error", JOptionPane.ERROR_MESSAGE);
	        } else {
	            // Gọi hàm addToFavourite với itemId và email của người dùng
	            updateQuantity(productId, nowPerson.getEmail(), quantity+1);
	            cartScreen.loadProductList();
	            cartScreen.displayProducts(cartScreen.productList);
	        }
	    });
	    	    
	    JButton minusButton = new JButton(" - ");
	    minusButton.setAlignmentX(CENTER_ALIGNMENT);
	    minusButton.addActionListener(e -> {
	    	Person nowPerson = cartScreen.getNowPerson();
	        if (nowPerson == null) {
	            JOptionPane.showMessageDialog(this, "You must be logged in to do this.", "Error", JOptionPane.ERROR_MESSAGE);
	        } else {
	            // Gọi hàm addToFavourite với itemId và email của người dùng
	            updateQuantity(productId, nowPerson.getEmail(), quantity-1);
	            cartScreen.loadProductList();
	            cartScreen.displayProducts(cartScreen.productList);
	        }
	    });	   
	    
	    // Tạo container cho các nút
	    JPanel buttonContainer = new JPanel();
	    buttonContainer.setLayout(new FlowLayout(FlowLayout.CENTER));
	    buttonContainer.add(addToFavouriteButton);
	    buttonContainer.add(removeFromCartButton);  // Thêm nút "Remove from Cart" vào container
	    buttonContainer.add(minusButton);
	    buttonContainer.add(plusButton);
	    
	    // Thêm các thành phần vào JPanel
	    this.add(Box.createVerticalGlue());
	    this.add(typeLabel);
	    this.add(idLabel);
	    this.add(priceLabel);
	    this.add(materialLabel);
	    this.add(subtypeLabel);
	    this.add(sizeLabel);
	    this.add(quantityLabel);
	    this.add(Box.createVerticalGlue());
	    this.add(buttonContainer);
	}
    
    // Constructor cho ProductPanel trong ManagerStore
    public ProductPanel(String productId, String title, float price, String material, String subtype, Integer size, String type, ManagerStoreScreen managerStoreScreen) {
        this.productId = productId;
        this.title = title;
        this.price = price;
        this.material = material;
        this.subtype = subtype;
        this.size = size;
        this.type = type;
        this.managerStoreScreen = managerStoreScreen;

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

        // Tạo container cho nút
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        // Nút "Remove"
        JButton removeButton = new JButton("Remove");
        removeButton.setAlignmentX(CENTER_ALIGNMENT);
        removeButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this product?", "Confirm Remove", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                removeProduct(productId);
                managerStoreScreen.loadProductList();    
                managerStoreScreen.displayProducts(managerStoreScreen.productList);
            }
        });
        buttonContainer.add(removeButton);
 
        JButton changeQualityButton = new JButton("Change Properties");
        changeQualityButton.setAlignmentX(CENTER_ALIGNMENT);
        changeQualityButton.addActionListener(e -> showChangeQualityDialog());
        buttonContainer.add(changeQualityButton);
        
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
    public static ProductPanel createFromResultSet(ResultSet resultSet, StoreScreen storeScreen) throws SQLException {
        String productId = resultSet.getString("id");
        String title = resultSet.getString("title");
        float price = resultSet.getFloat("price");
        String material = resultSet.getString("material");
        String subtype = resultSet.getString("subtype");
        Integer size = resultSet.getObject("size") != null ? resultSet.getInt("size") : null;
        String type = resultSet.getString("type");

        return new ProductPanel(productId, title, price, material, subtype, size, type, storeScreen);  // Thêm mainMenu vào constructor
    }

    // Phương thức tạo ProductPanel từ ResultSet cho ManagerStore
    public static ProductPanel createFromResultSet(ResultSet resultSet, ManagerStoreScreen managerStoreScreen) throws SQLException {
        String productId = resultSet.getString("id");
        String title = resultSet.getString("title");
        float price = resultSet.getFloat("price");
        String material = resultSet.getString("material");
        String subtype = resultSet.getString("subtype");
        Integer size = resultSet.getObject("size") != null ? resultSet.getInt("size") : null;
        String type = resultSet.getString("type");

        return new ProductPanel(productId, title, price, material, subtype, size, type, managerStoreScreen);  // Thêm mainMenu vào constructor
    }

    // Phương thức tạo ProductPanel từ ResultSet cho Favourite
    public static ProductPanel createFromResultSet(ResultSet resultSet, FavouriteScreen favouriteScreen) throws SQLException {
        String productId = resultSet.getString("id");
        String title = resultSet.getString("title");
        float price = resultSet.getFloat("price");
        String material = resultSet.getString("material");
        String subtype = resultSet.getString("subtype");
        Integer size = resultSet.getObject("size") != null ? resultSet.getInt("size") : null;
        String type = resultSet.getString("type");
       
        return new ProductPanel(productId, title, price, material, subtype, size, type, favouriteScreen);  // Thêm mainMenu vào constructor
    }
    
    // Phương thức tạo ProductPanel từ ResultSet cho Cart
    public static ProductPanel createFromResultSet(ResultSet resultSet, CartScreen cartScreen) throws SQLException {
        String productId = resultSet.getString("id");
        String title = resultSet.getString("title");
        float price = resultSet.getFloat("price");
        String material = resultSet.getString("material");
        String subtype = resultSet.getString("subtype");
        Integer size = resultSet.getObject("size") != null ? resultSet.getInt("size") : null;
        String type = resultSet.getString("type");
        int quantity = resultSet.getInt("quantity");
        cartScreen.setTotalPrice(cartScreen.getTotalPrice() + price * quantity); 

        return new ProductPanel(productId, title, price, material, subtype, size, type, quantity, cartScreen);  // Thêm mainMenu vào constructor
    }

    // Hàm xóa item khỏi giỏ hàng trong database
    private void removeProductFromCart(String productId, String email) {
    	String query = "DELETE FROM cart WHERE email = ? AND id_item = ?";
    	try (Connection connection = DatabaseConnection.getConnection();
    		PreparedStatement stmt = connection.prepareStatement(query)) {
	        stmt.setString(1, email);
	        stmt.setString(2, productId);

	        int rowsAffected = stmt.executeUpdate();
	        if (rowsAffected > 0) {
	            JOptionPane.showMessageDialog(this, "Item removed from cart successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
	            // Cập nhật lại giao diện (có thể là xóa khỏi danh sách hiển thị hoặc thông báo)
	        } else {
	            JOptionPane.showMessageDialog(this, "Failed to remove item from cart.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "An error occurred while removing item from cart.", "Error", JOptionPane.ERROR_MESSAGE);
	    }
    }

    // Hàm xóa item khỏi danh sách yêu thích trong database
    private void removeProductFavourite(String productId, String email) {
        String query = "DELETE FROM favourite WHERE email = ? AND id_item = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, productId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Item removed from favourites successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to remove item from favourites.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while removing item from favourites.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
      
    // Phương thức thêm vào giỏ hàng
    private void addToCart(String itemId, String userEmail) {
        
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

    // Phương thức thay đổi thuộc tính của sản phẩm
    private void showChangeQualityDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Change Quality", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 300);

        String[] fields = {"Title", "Price", "Material", "Subtype", "Size", "Type"};
        Object[] currentValues = {title, price, material, subtype, size, type};

        JPanel tablePanel = new JPanel(new GridLayout(fields.length, 3, 10, 10));

        JTextField[] newValues = new JTextField[fields.length];
        for (int i = 0; i < fields.length; i++) {
            tablePanel.add(new JLabel(fields[i]));
            tablePanel.add(new JLabel(currentValues[i] != null ? currentValues[i].toString() : "NULL"));
            newValues[i] = new JTextField();
            tablePanel.add(newValues[i]);
        }

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> {
            try {
                String newTitle = newValues[0].getText().isEmpty() ? title : "NULL".equalsIgnoreCase(newValues[0].getText()) ? null : newValues[0].getText();
                Float newPrice = newValues[1].getText().isEmpty() ? price : "NULL".equalsIgnoreCase(newValues[1].getText()) ? null : Float.parseFloat(newValues[1].getText());
                String newMaterial = newValues[2].getText().isEmpty() ? material : "NULL".equalsIgnoreCase(newValues[2].getText()) ? null : newValues[2].getText();
                String newSubtype = newValues[3].getText().isEmpty() ? subtype : "NULL".equalsIgnoreCase(newValues[3].getText()) ? null : newValues[3].getText();
                Integer newSize = newValues[4].getText().isEmpty() ? size : "NULL".equalsIgnoreCase(newValues[4].getText()) ? null : Integer.parseInt(newValues[4].getText());
                String newType = newValues[5].getText().isEmpty() ? type : "NULL".equalsIgnoreCase(newValues[5].getText()) ? null : newValues[5].getText();

                updateProductInDatabase(productId, newTitle, newPrice, newMaterial, newSubtype, newSize, newType);
                dialog.dispose();
                managerStoreScreen.loadProductList();
                managerStoreScreen.displayProducts(managerStoreScreen.productList);
                 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(confirmButton);

        dialog.add(new JScrollPane(tablePanel), BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void updateProductInDatabase(String productId, String title, Float price, String material, String subtype, Integer size, String type) {
    	String query = "UPDATE item SET title = ?, price = ?, material = ?, subtype = ?, size = ?, type = ? WHERE id = ?";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
            // Mở kết nối cơ sở dữ liệu
            connection = DatabaseConnection.getConnection();
            preparedStatement = connection.prepareStatement(query);
            
         // Thiết lập các tham số
            preparedStatement.setString(1, title);           // Cột 'title'
            if (price != null) {
                preparedStatement.setFloat(2, price);        // Cột 'price'
            } else {
                preparedStatement.setNull(2, java.sql.Types.FLOAT);
            }
            preparedStatement.setString(3, material);        // Cột 'material'
            preparedStatement.setString(4, subtype);         // Cột 'subtype'
            if (size != null) {
                preparedStatement.setInt(5, size);           // Cột 'size'
            } else {
                preparedStatement.setNull(5, java.sql.Types.INTEGER);
            }
            preparedStatement.setString(6, type);            // Cột 'type'
            preparedStatement.setString(7, productId);       // Điều kiện WHERE
            int rowsAffected = preparedStatement.executeUpdate();
            

            if (rowsAffected > 0) {
            	String string = "Product qualities have been updated.";
                JOptionPane.showMessageDialog((JFrame) SwingUtilities.getWindowAncestor(this), string, "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog((JFrame) SwingUtilities.getWindowAncestor(this), "No change has been done.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog((JFrame) SwingUtilities.getWindowAncestor(this), "An error occurred while connecting to the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateQuantity(String productId, String userEmail, int quantity) {
        int newQuantity = 0;
        String query = "UPDATE cart SET quantity = ? WHERE id_item = ? AND email = ?";
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, quantity);
            stmt.setString(2, productId);
            stmt.setString(3, userEmail);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update item quantity in the cart.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Không thể thay đổi quantity");
            return;
        }
    }
     
    private void removeProduct(String productId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "DELETE FROM item WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, productId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Product removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                managerStoreScreen.loadProductList(); 
                managerStoreScreen.displayProducts(managerStoreScreen.productList);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to remove product.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while removing the product.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String getProductId() {
		return productId;
	}

	public String getTitle() {
		return title;
	}

	public float getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}
    
}
