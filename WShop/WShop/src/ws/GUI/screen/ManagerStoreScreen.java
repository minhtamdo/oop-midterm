package ws.GUI.screen;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import ws.GUI.main.ManagerMenu;
import ws.GUI.product.ProductPanel;
import ws.Manager.Order;
import ws.Manager.OrderItem;
import ws.main.DatabaseConnection;
import ws.person.Person;

public class ManagerStoreScreen extends JPanel {
	private JFrame parentFrame;
	private int page = 1; 
    public List<ProductPanel> productList = new ArrayList<>();
    private JPanel productPanel = new JPanel();
    private Person nowPerson = null;
    private ManagerMenu managerMenu;
    public ManagerStoreScreen(JFrame parentFrame, ManagerMenu managerM) {
    	this.parentFrame = parentFrame;
    	managerMenu = managerM;
    	nowPerson = managerM.getNowPerson();
        setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel storeName = new JLabel("9S Fashion Store       ");
        storeName.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(storeName);

        // Các ô tìm kiếm và sắp xếp
        JTextField searchField = new JTextField(20);
        headerPanel.add(searchField);

        String[] searchOptions = {"Search by Name", "Search by ID", "Search by Type"};
        JComboBox<String> searchType = new JComboBox<>(searchOptions);
        headerPanel.add(searchType);

        String[] sortOptions = {"Sort by Name", "Sort by Price", "Sort by Type"};
        JComboBox<String> sortType = new JComboBox<>(sortOptions);
        headerPanel.add(sortType);

        String[] sortSubOptions = {"Asc", "Desc"};
        JComboBox<String> sortSubType = new JComboBox<>(sortSubOptions);
        headerPanel.add(sortSubType);

        JButton searchButton = new JButton("Search");
        headerPanel.add(searchButton);

        JButton resetButton = new JButton("Reset");
        headerPanel.add(resetButton);

        // Tạo bảng sản phẩm
        productPanel.setLayout(new GridLayout(4, 3, 10, 10));
        loadProductList();  // Hiển thị sản phẩm từ database khi bắt đầu

        // Nút chuyển trang
        JPanel paginationPanel = new JPanel();
        JButton prevPageButton = new JButton("Previous");
        JButton nextPageButton = new JButton("Next");
        paginationPanel.add(prevPageButton);
        paginationPanel.add(nextPageButton);

        prevPageButton.addActionListener(e -> {
            if (page > 1) {
                page--;
                displayProducts(productList);
            }
        });

        nextPageButton.addActionListener(e -> {
            page++;
            displayProducts(productList);
        });

        // Nút reset
        resetButton.addActionListener(e -> {
            page = 1;
            loadProductList();
            displayProducts(productList);
        });
        
        // Trong phương thức khởi tạo ManagerStoreScreen
        JButton addItemButton = new JButton("Add Item");
        headerPanel.add(addItemButton); // Thêm nút vào headerPanel ở vị trí cuối cùng (góc trên bên phải)
        // nút Orders
        JButton ordersButton = new JButton("Orders");
        ordersButton.setAlignmentX(CENTER_ALIGNMENT);

        // Xử lý sự kiện khi nút Orders được nhấn
        ordersButton.addActionListener(e -> showOrdersDialog());

        headerPanel.add(ordersButton);  // Thêm nút "Orders" vào headerPanel

        // Sự kiện khi nhấn nút "Add Item"
        addItemButton.addActionListener(e -> {
            showAddProductDialog(); // Gọi phương thức hiển thị dialog để thêm sản phẩm
        });
        
        // Thêm các phần vào cửa sổ
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(productPanel, BorderLayout.CENTER);
        add(paginationPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
                String searchBy = (String) searchType.getSelectedItem();
                String sortBy = (String) sortType.getSelectedItem();
                String sortOrder = (String) sortSubType.getSelectedItem();

                // Gọi hàm tìm kiếm và sắp xếp với các tham số mới
                searchProducts(searchBy, searchText, sortBy, sortOrder);
            }
        });
        
    }
    
    
    private void showOrdersDialog() {
        if (nowPerson == null) {
            JOptionPane.showMessageDialog(this, "You must be logged in to view your orders.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lấy danh sách đơn hàng của người dùng từ cơ sở dữ liệu
        List<Order> orders = getOrdersFromDatabase(nowPerson.getEmail());

        // Tạo một Dialog để hiển thị danh sách đơn hàng
        JDialog ordersDialog = new JDialog(parentFrame, "Your Orders", true);
        ordersDialog.setLayout(new BorderLayout());

        // Tạo bảng để hiển thị các đơn hàng
        JPanel ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));

        for (Order order : orders) {
            JPanel orderPanel = new JPanel();
            orderPanel.setLayout(new BoxLayout(orderPanel, BoxLayout.Y_AXIS));
            
            JLabel orderLabel = new JLabel(String.format("Order ID: %d | Address: %s | Phone: %s | Time: %s | Total: $%.2f | Status: %s",
                    order.getId(), order.getAddress(), order.getPnum(), order.getOrderTime(), order.getTotal(), order.getAttitude()));
           
            orderPanel.add(orderLabel);

            // Nút để xem sản phẩm trong đơn hàng
            JButton viewItemsButton = new JButton("View Items");
            viewItemsButton.addActionListener(e -> showOrderItemsDialog(order.getId()));
            orderPanel.add(viewItemsButton);

            JButton updateButton = new JButton("Update");
            updateButton.addActionListener(e -> showUpdateOrderDialog(order, ordersDialog));
            orderPanel.add(updateButton);
 
            ordersPanel.add(orderPanel);
        }

        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        ordersDialog.add(scrollPane, BorderLayout.CENTER);

        // Nút đóng dialog
        JButton closeButton = new JButton("Close"); 
        closeButton.addActionListener(e -> ordersDialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        ordersDialog.add(buttonPanel, BorderLayout.SOUTH);

        ordersDialog.setSize(850, 400);
        ordersDialog.setLocationRelativeTo(this);
        ordersDialog.setVisible(true);
    }

    private void showUpdateOrderDialog(Order order, JDialog orderDialog) {
        // Tạo dialog
        JDialog updateDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Update Order", true);
        updateDialog.setLayout(new BorderLayout());

        // Panel chính
        JPanel updatePanel = new JPanel();
        updatePanel.setLayout(new GridLayout(1, 2, 10, 10));

        // Trường Attitude
        updatePanel.add(new JLabel("Status:"));
        JTextField statusField = new JTextField(order.getAttitude());
        updatePanel.add(statusField);

        updateDialog.add(updatePanel, BorderLayout.CENTER);

        // Nút Save
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String newStatus = statusField.getText().trim();

            // Cập nhật thông tin trong cơ sở dữ liệu
            if (updateOrderInDatabase(order.getId(), newStatus)) {
                JOptionPane.showMessageDialog(updateDialog, "Order updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                updateDialog.dispose();
                orderDialog.dispose();
                showOrdersDialog();
            } else {
                JOptionPane.showMessageDialog(updateDialog, "Failed to update order.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Nút Cancel
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> updateDialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        updateDialog.add(buttonPanel, BorderLayout.SOUTH);

        updateDialog.setSize(400, 300);
        updateDialog.setLocationRelativeTo(this);
        updateDialog.setVisible(true);
    }

    private boolean updateOrderInDatabase(int orderId, String status) {
        String query = "UPDATE \"order\" SET attitude = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection()) {
        	PreparedStatement stmt = connection.prepareStatement(query);
        	
            stmt.setString(1, status.isEmpty() ? null : status);
            stmt.setInt(2, orderId);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Hàm lấy danh sách đơn hàng từ cơ sở dữ liệu
    private List<Order> getOrdersFromDatabase(String email) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT id, email, attitude, address, phone_number, time, total "
        		+ "FROM \"order\" "
        		+ "ORDER BY id DESC";
        try (Connection connection = DatabaseConnection.getConnection()) {
        	PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String address = rs.getString("address");
                String pnum = rs.getString("phone_number");
                String orderTime = rs.getString("time"); // Chuyển từ timestamp thành String
                float total = rs.getFloat("total");
                String attitude = rs.getString("attitude");
                
                Order order = new Order(id, email, attitude, address, pnum, orderTime, total);
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Hàm hiển thị danh sách sản phẩm trong một đơn hàng
    private void showOrderItemsDialog(int orderId) {
        List<OrderItem> orderItems = getOrderItemsFromDatabase(orderId);

        JDialog orderItemsDialog = new JDialog(parentFrame, "Order Items", true);
        orderItemsDialog.setLayout(new BorderLayout());

        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));

        for (OrderItem item : orderItems) {
            String displayText = String.format("Product ID: %s | Title: %s | Price: $%.2f | Quantity: %d",
                    item.getProductId(), item.getTitle(), item.getPrice(), item.getQuantity());

//            // Kiểm tra nếu title là "Product not available", có thể thay đổi cách hiển thị
//            if ("Product not available".equals(item.getTitle())) {
//                displayText = "Product ID: " + item.getProductId() + " | " + item.getTitle();
//            }
            
            JLabel itemLabel = new JLabel(displayText);
            itemsPanel.add(itemLabel);
        }

        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        orderItemsDialog.add(scrollPane, BorderLayout.CENTER);

        // Nút đóng dialog
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> orderItemsDialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        orderItemsDialog.add(buttonPanel, BorderLayout.SOUTH);

        orderItemsDialog.setSize(500, 200);
        orderItemsDialog.setLocationRelativeTo(this);
        orderItemsDialog.setVisible(true);
    }

    // Hàm lấy danh sách sản phẩm trong một đơn hàng từ cơ sở dữ liệu
    private List<OrderItem> getOrderItemsFromDatabase(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String query = "SELECT oi.id_item, oi.id_order, oi.quantity, oi.price, i.title " +
                "FROM order_item oi " +
                "LEFT JOIN item i ON oi.id_item = i.id " + 
                "ORDER BY id_item DESC";
        try (Connection connection = DatabaseConnection.getConnection()) {
        	PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String productId = rs.getString("id_item");
                String title = rs.getString("title");
                if (title == null) {
                    title = "N/A";
                }
                int quantity = rs.getInt("quantity");
                float price = rs.getFloat("price");

                OrderItem orderItem = new OrderItem(productId, title, orderId, quantity, price); // Thêm title vào constructor
                orderItems.add(orderItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItems;
    }

    private void showSignupDialog() {
        JDialog signupDialog = new JDialog(parentFrame, "Sign Up", true);
        signupDialog.setSize(400, 300);
        signupDialog.setLocationRelativeTo(this);

        JPanel signupPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        signupPanel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        signupPanel.add(usernameField);

        signupPanel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        signupPanel.add(passwordField);

        signupPanel.add(new JLabel("Name:"));
        JTextField nameField = new JTextField();
        signupPanel.add(nameField);

        signupPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField();
        signupPanel.add(emailField);

        signupPanel.add(new JLabel("Phone:"));
        JTextField phoneField = new JTextField();
        signupPanel.add(phoneField);

        JButton signupConfirmButton = new JButton("Sign Up");
        signupPanel.add(new JLabel());  // Placeholder
        signupPanel.add(signupConfirmButton);

        signupDialog.add(signupPanel);

        // Sự kiện khi nhấn nút "Sign Up"
        signupConfirmButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String role = "Guest"; // Mặc định role là Guest

            if (username.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(signupDialog, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO person (username, password, name, email, phone, role) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, name);
                stmt.setString(4, email);
                stmt.setString(5, phone);
                stmt.setString(6, role);

                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(signupDialog, "Sign up successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    signupDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(signupDialog, "Sign up failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(signupDialog, "An error occurred while connecting to the database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        signupDialog.pack();
        signupDialog.setVisible(true);
    }
    
    private void showUserInfoDialog() {
        if (nowPerson != null) {
            JDialog userInfoDialog = new JDialog(parentFrame, "User Information", true);  // Tạo dialog
            userInfoDialog.setSize(350, 300);  // Điều chỉnh kích thước dialog
            userInfoDialog.setLocationRelativeTo(this);

            // Tạo panel chứa thông tin người dùng với 3 cột
            JPanel infoPanel = new JPanel(new GridLayout(7, 3, 10, 10));  // 7 hàng, 3 cột

            // Thêm thông tin vào panel
            infoPanel.add(new JLabel("ID:"));
            infoPanel.add(new JLabel(String.valueOf(nowPerson.getId())));
            infoPanel.add(new JLabel());  // Cột thứ ba trống

            infoPanel.add(new JLabel("Username:"));
            infoPanel.add(new JLabel(nowPerson.getUsername()));
            infoPanel.add(new JLabel());  // Cột thứ ba trống

            infoPanel.add(new JLabel("Name:"));
            infoPanel.add(new JLabel(nowPerson.getName()));
            infoPanel.add(new JLabel());  // Cột thứ ba trống

            infoPanel.add(new JLabel("Email:"));
            infoPanel.add(new JLabel(nowPerson.getEmail()));
            infoPanel.add(new JLabel());  // Cột thứ ba trống

            infoPanel.add(new JLabel("Phone:"));
            infoPanel.add(new JLabel(nowPerson.getPhone()));
            infoPanel.add(new JLabel());  // Cột thứ ba trống

            infoPanel.add(new JLabel("Role:"));
            infoPanel.add(new JLabel(nowPerson.getRole()));
            infoPanel.add(new JLabel());  // Cột thứ ba trống

            // Nút Close sẽ được đặt vào cột thứ ba
            infoPanel.add(new JLabel());  // Placeholder cho hàng cuối cùng
            infoPanel.add(new JLabel());  // Placeholder
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> userInfoDialog.dispose());  // Đóng dialog khi nhấn Close
            infoPanel.add(closeButton);  // Thêm nút vào cột thứ ba

            // Đặt panel thông tin vào dialog
            userInfoDialog.add(infoPanel);
            userInfoDialog.setVisible(true);  // Hiển thị dialog
        } else {
            JOptionPane.showMessageDialog(parentFrame, "No user is logged in.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showLoginDialog() {
        JDialog loginDialog = new JDialog(parentFrame, "Log In", true);
        loginDialog.setSize(300, 200);
        loginDialog.setLocationRelativeTo(this);

        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        loginPanel.add(new JLabel("Username:"));
        JTextField usernameField = new JTextField();
        loginPanel.add(usernameField);

        loginPanel.add(new JLabel("Password:"));
        JPasswordField passwordField = new JPasswordField();
        loginPanel.add(passwordField);

        JButton loginConfirmButton = new JButton("Log In");
        loginPanel.add(new JLabel());  // Placeholder
        loginPanel.add(loginConfirmButton);

        loginDialog.add(loginPanel);

        // Gắn sự kiện cho nút Log In
        loginConfirmButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginDialog, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "SELECT * FROM person WHERE username = ? AND password = ?";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    // Gán thông tin vào nowPerson
                    nowPerson = new Person(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("role")
                    );
                    JOptionPane.showMessageDialog(loginDialog, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    								managerMenu.updateNowPerson(nowPerson);           
                    loginDialog.dispose();  // Đóng dialog
                } else {
                    JOptionPane.showMessageDialog(loginDialog, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(loginDialog, "An error occurred while connecting to the database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginDialog.pack();  // Sắp xếp lại giao diện
        loginDialog.setVisible(true);  // Hiển thị dialog
    }

    public void loadProductList() {
        productList.clear();  // Xóa danh sách sản phẩm cũ

        ResultSet resultSet = getProductsFromDatabase();
        try {
            while (resultSet.next()) {
                ProductPanel product = ProductPanel.createFromResultSet(resultSet,this);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        displayProducts(productList);  // Hiển thị sản phẩm lên giao diện
    }

    private ResultSet getProductsFromDatabase() {
        String query = "SELECT * FROM item ORDER BY id ASC";
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void displayProducts(List<ProductPanel> products) {
        productPanel.removeAll();
        
        int start = (page - 1) * 12;
        int end = Math.min(start + 12, products.size());

        for (int i = start; i < end; i++) {
            productPanel.add(products.get(i));
        }

        productPanel.revalidate();
        productPanel.repaint();
    }

    private void searchProducts(String searchBy, String searchText, String sortBy, String sortOrder) {
        // Xây dựng câu lệnh SQL tùy thuộc vào tham số tìm kiếm
        String column = "";
        if (searchBy.equals("Search by Name")) {
            column = "title";  // Trường trong database là "title"
        } else if (searchBy.equals("Search by ID")) {
            column = "id";  // Trường trong database là "id"
        } else if (searchBy.equals("Search by Type")) {
            column = "type";  // Trường trong database là "type"
        }

        // Sắp xếp theo trường và thứ tự
        String sortColumn = "";
        if (sortBy.equals("Sort by Name")) {
            sortColumn = "title";  // Sắp xếp theo "title"
        } else if (sortBy.equals("Sort by Price")) {
            sortColumn = "price";  // Sắp xếp theo "id"
        } else if (sortBy.equals("Sort by Type")) {
            sortColumn = "type";  // Sắp xếp theo "type"
        }

        // Sắp xếp theo thứ tự Ascending (tăng dần) hoặc Descending (giảm dần)
        String order = sortOrder.equals("Asc") ? "ASC" : "DESC";  // Lựa chọn ASC hoặc DESC

        // Xây dựng câu lệnh SQL với sắp xếp và tìm kiếm
        String query = "SELECT * FROM item WHERE LOWER(" + column + ") LIKE LOWER(?) ORDER BY " + sortColumn + " " + order;

        // Tạo câu lệnh SQL và sử dụng PreparedStatement
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, "%" + searchText + "%");
            ResultSet resultSet = stmt.executeQuery();
            productList.clear();  
            while (resultSet.next()) {
                ProductPanel product = ProductPanel.createFromResultSet(resultSet,this);
                productList.add(product);
            }
            page = 1;
            displayProducts(productList);  
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAddProductDialog() {
        JDialog addProductDialog = new JDialog(parentFrame, "Add New Product", true);
        addProductDialog.setSize(500, 400);
        addProductDialog.setLocationRelativeTo(this);

        JPanel addProductPanel = new JPanel(new GridLayout(8, 2, 10, 10));

        addProductPanel.add(new JLabel("Product ID:"));
        JTextField idField = new JTextField();
        addProductPanel.add(idField);

        addProductPanel.add(new JLabel("Title:"));
        JTextField titleField = new JTextField();
        addProductPanel.add(titleField);

        addProductPanel.add(new JLabel("Price:"));
        JTextField priceField = new JTextField();
        addProductPanel.add(priceField);

        addProductPanel.add(new JLabel("Type:"));
        JTextField typeField = new JTextField();
        addProductPanel.add(typeField);

        addProductPanel.add(new JLabel("Material:"));
        JTextField materialField = new JTextField();
        addProductPanel.add(materialField);

        addProductPanel.add(new JLabel("Subtype:"));
        JTextField subtypeField = new JTextField();
        addProductPanel.add(subtypeField);

        addProductPanel.add(new JLabel("Size:"));
        JTextField sizeField = new JTextField();
        addProductPanel.add(sizeField);

        JButton addConfirmButton = new JButton("Add Product");
        addProductPanel.add(new JLabel()); // Placeholder
        addProductPanel.add(addConfirmButton);

        addProductDialog.add(addProductPanel);

        addConfirmButton.addActionListener(e -> {
            String id = idField.getText();
            String title = titleField.getText();
            String price = priceField.getText();
            String type = typeField.getText();
            String material = materialField.getText();
            String subtype = subtypeField.getText();
            String size = sizeField.getText();

            if (id.isEmpty() || title.isEmpty() || price.isEmpty() || type.isEmpty() || material.isEmpty() || subtype.isEmpty() || size.isEmpty()) {
                JOptionPane.showMessageDialog(addProductDialog, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection connection = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO item (id, title, price, type, material, subtype, size) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(query);
                stmt.setString(1, id);
                stmt.setString(2, title);
                stmt.setDouble(3, Double.parseDouble(price));
                stmt.setString(4, type);
                stmt.setString(5, material);
                stmt.setString(6, subtype);
                stmt.setInt(7, Integer.parseInt(size));

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(addProductDialog, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    addProductDialog.dispose();
                    loadProductList(); // Tải lại danh sách sản phẩm
                    displayProducts(productList); // Hiển thị sản phẩm mới
                } else {
                    JOptionPane.showMessageDialog(addProductDialog, "Failed to add product.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException | NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(addProductDialog, "An error occurred. Please check your input.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        addProductDialog.pack();
        addProductDialog.setVisible(true);
    }
   
    private ResultSet executeQuery(String query) {
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public Person getNowPerson() {
        return nowPerson;
    }
    
}

