package ws.GUI.screen;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import ws.GUI.main.MainMenu;
import ws.GUI.product.ProductPanel;
import ws.Manager.Order;
import ws.Manager.OrderItem;
import ws.main.DatabaseConnection;
import ws.person.Person;

public class CartScreen extends JPanel {
	private JFrame parentFrame;
	private int page = 1; 
    public List<ProductPanel> productList = new ArrayList<>();
    private JPanel productPanel = new JPanel();
    private Person nowPerson = null;
    private JButton loginButton;
    private JButton logoutButton;
    private JButton signUpButton;
    private JButton userButton;
    private MainMenu mainMenu;
    private float totalPrice;
    private JLabel totalPriceLabel = new JLabel("Total Price: $0.00");
    public CartScreen(JFrame parentFrame, MainMenu mainM) {
    	this.parentFrame = parentFrame;
    	mainMenu = mainM;
    	nowPerson = mainM.getNowPerson();
        setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JLabel storeName = new JLabel("Cart      ");
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

        loginButton = new JButton("Log In");
        logoutButton = new JButton("Log Out");
        signUpButton = new JButton("Sign Up");
        userButton = new JButton(" User ");
        
        headerPanel.add(loginButton);
        headerPanel.add(signUpButton);
        headerPanel.add(logoutButton);
        headerPanel.add(userButton);
        if (nowPerson != null) {
            // Nếu đã đăng nhập, hiển thị nút "User" và "Log Out"
            userButton.setVisible(true);  // Hiển thị nút User
            logoutButton.setVisible(true);  // Hiển thị nút Log Out
            loginButton.setVisible(false);  // Ẩn nút Log In
            signUpButton.setVisible(false);  // Ẩn nút Sign Up
        } else {
            // Nếu chưa đăng nhập, ẩn nút "User" và "Log Out"
            userButton.setVisible(false);
            logoutButton.setVisible(false);
            loginButton.setVisible(true);
            signUpButton.setVisible(true);
        }
        
        JButton buyButton = new JButton("Buy");
        buyButton.setForeground(Color.BLACK);
        buyButton.setFocusPainted(false);

        // Xử lý sự kiện khi nhấn nút "Buy"
        buyButton.addActionListener(e -> {
            if (nowPerson == null) {
                JOptionPane.showMessageDialog(this, "You must log in to make a purchase.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (totalPrice == 0) {
                JOptionPane.showMessageDialog(this, "Your cart is empty. Add items to your cart before buying.", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                processPurchase();
            }
        });
        
        headerPanel.add(buyButton);
        
        userButton.addActionListener(e -> {
        	showUserInfoDialog();
        });
       
        loginButton.addActionListener(e -> {
            // Logic cho nút "Log In" (chưa triển khai logic)
            showLoginDialog();
        });

        signUpButton.addActionListener(e -> {
            // Logic cho nút "Sign Up" (chưa triển khai logic)
        	showSignupDialog();
        });

        logoutButton.addActionListener(e -> {
            // Logic cho nút "Log Out" (chưa triển khai logic)
            System.out.println("Log Out button clicked");
            nowPerson = null;  // Đăng xuất, set lại nowPerson là null
            updatePersonInMainMenu(nowPerson);
            updateLoginUI();  // Cập nhật lại giao diện sau khi đăng xuất
            page = 1;
            productList.clear();
            displayProducts(productList);
        });    
        // Nút reset
        resetButton.addActionListener(e -> {
            page = 1;
            loadProductList();
            displayProducts(productList);
        });
        
        // nút Orders
        JButton ordersButton = new JButton("Orders");
        ordersButton.setAlignmentX(CENTER_ALIGNMENT);

        // Xử lý sự kiện khi nút Orders được nhấn
        ordersButton.addActionListener(e -> showOrdersDialog());

        headerPanel.add(ordersButton);  // Thêm nút "Orders" vào headerPanel
   
        // Tạo bảng sản phẩm
        productPanel.setLayout(new GridLayout(4, 3, 10, 10));
        loadProductList();  // Hiển thị sản phẩm từ database khi bắt đầu

        // Nút chuyển trang
        JPanel paginationPanel = new JPanel();
        JButton prevPageButton = new JButton("Previous");
        JButton nextPageButton = new JButton("Next");
        paginationPanel.add(new JLabel("                                    "
        		+ "            "), BorderLayout.EAST);
        paginationPanel.add(prevPageButton, BorderLayout.CENTER);
        paginationPanel.add(nextPageButton, BorderLayout.CENTER);
        totalPriceLabel.setFont(new Font(totalPriceLabel.getFont().getName(), Font.BOLD, 16));
        totalPriceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        paginationPanel.add(totalPriceLabel, BorderLayout.EAST);
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
                    updateLoginUI();  // Cập nhật giao diện sau khi đăng nhập
                    updatePersonInMainMenu(nowPerson);
                    page = 1;
                    loadProductList();
                    displayProducts(productList);
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

    private void updateLoginUI() {
        if (nowPerson != null) {
            // Nếu đã đăng nhập, hiển thị nút "User" và "Log Out"
            userButton.setVisible(true);  // Hiển thị nút User
            logoutButton.setVisible(true);  // Hiển thị nút Log Out
            loginButton.setVisible(false);  // Ẩn nút Log In
            signUpButton.setVisible(false);  // Ẩn nút Sign Up
        } else {
            // Nếu chưa đăng nhập, ẩn nút "User" và "Log Out"
            userButton.setVisible(false);
            logoutButton.setVisible(false);
            loginButton.setVisible(true);
            signUpButton.setVisible(true);
        }
    }	
    public void loadProductList() {
    	if (nowPerson == null || nowPerson.getEmail() == null) {
            return;
        }
    	productList.clear();  // Xóa danh sách sản phẩm cũ
    	this.setTotalPrice(0);
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
        updateTotalPrice();
    }



	private ResultSet getProductsFromDatabase() {
        if (nowPerson == null || nowPerson.getEmail() == null) {
            return null;
        }
        String query = "SELECT i.*, c.quantity FROM cart c " +
                       "JOIN item i ON c.id_item = i.id " +
                       "WHERE c.email = ? " + 
                       "ORDER BY id ASC";
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, nowPerson.getEmail()); // Gán email của người dùng hiện tại
            return stmt.executeQuery();
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
        if (nowPerson == null || nowPerson.getEmail() == null) {
            return;
        }

        String column = "";
        if (searchBy.equals("Search by Name")) {
            column = "i.title";  // Trường trong bảng item là "title"
        } else if (searchBy.equals("Search by ID")) {
            column = "i.id";  // Trường trong bảng item là "id"
        } else if (searchBy.equals("Search by Type")) {
            column = "i.type";  // Trường trong bảng item là "type"
        }

        // Sắp xếp theo trường và thứ tự
        String sortColumn = "";
        if (sortBy.equals("Sort by Name")) {
            sortColumn = "i.title";  // Sắp xếp theo "title"
        } else if (sortBy.equals("Sort by Price")) {
            sortColumn = "i.price";  // Sắp xếp theo "price"
        } else if (sortBy.equals("Sort by Type")) {
            sortColumn = "i.type";  // Sắp xếp theo "type"
        }

        // Sắp xếp theo thứ tự Ascending (tăng dần) hoặc Descending (giảm dần)
        String order = sortOrder.equals("Asc") ? "ASC" : "DESC";  // Lựa chọn ASC hoặc DESC

        // Xây dựng câu lệnh SQL với sắp xếp và tìm kiếm, có điều kiện email từ bảng "cart"
        String query = "SELECT i.*, c.quantity FROM cart c " +
                       "JOIN item i ON c.id_item = i.id " +
                       "WHERE f.email = ? AND LOWER(" + column + ") LIKE LOWER(?) " +
                       "ORDER BY " + sortColumn + " " + order;

        // Tạo câu lệnh SQL và sử dụng PreparedStatement
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, nowPerson.getEmail());  // Lấy email của nowPerson
            stmt.setString(2, "%" + searchText + "%");
            ResultSet resultSet = stmt.executeQuery();

            productList.clear();  // Xóa danh sách hiện tại trước khi thêm dữ liệu mới
            while (resultSet.next()) {
                ProductPanel product = ProductPanel.createFromResultSet(resultSet, this);
                // Bạn có thể bổ sung logic để xử lý thêm "quantity" nếu cần
                productList.add(product);
            }
            page = 1;  // Reset về trang đầu tiên
            displayProducts(productList);  // Hiển thị danh sách sản phẩm
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    
    private void processPurchase() {
        if (nowPerson == null) {
            JOptionPane.showMessageDialog(this, "You must log in to make a purchase.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (totalPrice <= 0.01) {
            JOptionPane.showMessageDialog(this, "Your cart is empty. Add items to your cart before buying.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Dialog nhập thông tin
        JTextField addressField = new JTextField();
        JTextField phoneField = new JTextField();

        Object[] inputFields = {
            "Shipping Address:", addressField,
            "Phone Number:", phoneField
        };

        int option = JOptionPane.showConfirmDialog(
            this, inputFields, "Enter Shipping Information", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();

            if (address.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Both address and phone number are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Tạo dialog xác nhận
            StringBuilder purchaseDetails = new StringBuilder();
            purchaseDetails.append("Purchase Details:\n\n");
            purchaseDetails.append("Buyer Email: ").append(nowPerson.getEmail()).append("\n");
            purchaseDetails.append("Shipping Address: ").append(address).append("\n");
            purchaseDetails.append("Phone Number: ").append(phone).append("\n\n");
            purchaseDetails.append("Items:\n");

            for (ProductPanel product : productList) {
                    purchaseDetails.append(String.format(
                        "- ID: %s | Name: %s | Price: $%.2f | Quantity: %d\n",
                    product.getProductId(), product.getTitle(), product.getPrice(), product.getQuantity()
                    ));
                
            }


            purchaseDetails.append(String.format("\nTotal Price: $%.2f", totalPrice));

            int confirmOption = JOptionPane.showConfirmDialog(
                this,
                new Object[]{new JScrollPane(new JTextArea(purchaseDetails.toString(), 15, 40))},
                "Confirm Purchase",
                JOptionPane.OK_CANCEL_OPTION
            );

            if (confirmOption == JOptionPane.OK_OPTION) {
                completePurchase(address, phone, totalPrice);
            }
        }
    }

    private void completePurchase(String address, String phone, float totalPrice) {
    	PreparedStatement orderStmt = null;
        PreparedStatement orderItemStmt = null;
        ResultSet generatedKeys = null;
        try (Connection connection = DatabaseConnection.getConnection()) {

        	String email = nowPerson.getEmail();

        	String orderSQL = "INSERT INTO \"order\" (email, attitude, address, phone_number, time, total) VALUES (?, ?, ?, ?, NOW(), ?)";
            orderStmt = connection.prepareStatement(orderSQL, Statement.RETURN_GENERATED_KEYS);
            orderStmt.setString(1, email);
            orderStmt.setString(2, "Pending"); // Trạng thái ban đầu là "đang chờ duyệt"
            orderStmt.setString(3, address);
            orderStmt.setString(4, phone);
            orderStmt.setFloat(5, totalPrice);
            orderStmt.executeUpdate();
            
            generatedKeys = orderStmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Failed to retrieve order ID.");
            }
            int orderId = generatedKeys.getInt(1);

            // 2. Thêm danh sách sản phẩm vào bảng "order_item"
            String orderItemSQL = "INSERT INTO order_item (id_item, id_order, quantity, price) VALUES (?, ?, ?, ?)";
            orderItemStmt = connection.prepareStatement(orderItemSQL);
            for (ProductPanel product : productList) {
                orderItemStmt.setString(1, product.getProductId());
                orderItemStmt.setInt(2, orderId);
                orderItemStmt.setInt(3, product.getQuantity());
                orderItemStmt.setFloat(4, product.getPrice());
                orderItemStmt.addBatch();
            }
            orderItemStmt.executeBatch();
            
            JOptionPane.showMessageDialog(this,
                "Purchase completed successfully!\n" +
                "Shipping to: " + address + "\n" +
                "Phone: " + phone + "\n" +
                "Total Price: $" + totalPrice,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Xóa giỏ hàng trong database
            String query = "DELETE FROM cart WHERE email = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, email);
            stmt.executeUpdate();
            
            // Làm trống giỏ hàng trong UI
            this.loadProductList();
            this.displayProducts(productList);
 
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error completing purchase. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
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
        updatePanel.setLayout(new GridLayout(2, 2, 10, 10));

        // Trường Address
        updatePanel.add(new JLabel("Address:"));
        JTextField addressField = new JTextField(order.getAddress());
        updatePanel.add(addressField);

        // Trường Phone Number
        updatePanel.add(new JLabel("Phone Number:"));
        JTextField phoneField = new JTextField(order.getPnum());
        updatePanel.add(phoneField);

        updateDialog.add(updatePanel, BorderLayout.CENTER);

        // Nút Save
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String newAddress = addressField.getText().trim();
            String newPhone = phoneField.getText().trim();

            // Cập nhật thông tin trong cơ sở dữ liệu
            if (updateOrderInDatabase(order.getId(), newAddress, newPhone)) {
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

    private boolean updateOrderInDatabase(int orderId, String address, String phone) {
        String query = "UPDATE \"order\" SET address = ?, phone_number = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection()) {
        	PreparedStatement stmt = connection.prepareStatement(query);
        	
            stmt.setString(1, address.isEmpty() ? null : address);
            stmt.setString(2, phone.isEmpty() ? null : phone);
            stmt.setInt(3, orderId);

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
        String query = "SELECT id, email, attitude, address, phone_number, time, total " +
        			   "FROM \"order\" " + 
        			   "WHERE email = ? " +
        			   "ORDER BY id DESC";
        try (Connection connection = DatabaseConnection.getConnection()) {
        	PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, email);
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
                "WHERE oi.id_order = ?";
        try (Connection connection = DatabaseConnection.getConnection()) {
        	PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, orderId);
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


    private void updateTotalPrice() {
        totalPriceLabel.setText(String.format("Total Price: $%.2f", totalPrice));
    }
    
    public float getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
    
    public Person getNowPerson() {
        return nowPerson;
    }
    
    public void updatePersonInMainMenu(Person newPerson) {
        mainMenu.updateNowPerson(newPerson);
    }
}

