package ws.main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import ws.person.Person;

public class MainMenu extends JFrame {
    private int page = 1;  // Biến lưu trang hiện tại
    private List<ProductPanel> productList = new ArrayList<>();
    private JPanel productPanel = new JPanel();
    private Person nowPerson = null;
    private JButton loginButton;
    private JButton logoutButton;
    private JButton signUpButton;
    private JButton userInforButton;
    private CardLayout cardLayout;  // Layout để chuyển đổi giữa các màn hình
    private JPanel contentPanel;  // Panel chính chứa các màn hình

    MainMenu() {
        // Cấu hình cửa sổ
        setTitle("9S Fashion Store");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        contentPanel.add(new StorePanel(), "store");
        contentPanel.add(new CartPanel(), "cart");
        contentPanel.add(new FavouritePanel(), "favourite");
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenuItem storeItem = new JMenuItem("Store");
        JMenuItem cartItem = new JMenuItem("Cart");
        JMenuItem favouriteItem = new JMenuItem("Favourite");
        
        storeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "store");  // Chuyển đến màn hình Store
            }
        });cartItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "cart");  // Chuyển đến màn hình Cart
            }
        });

        favouriteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "favourite");  // Chuyển đến màn hình Favourite
            }
        });
        
        menu.add(storeItem);
        menu.add(cartItem);
        menu.add(favouriteItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
        
        add(contentPanel);
        
        
        
        
        // Tạo thanh tiêu đề
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel storeName = new JLabel("9S Fashion Store");
        storeName.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(storeName);

        // Các ô tìm kiếm và sắp xếp
        JTextField searchField = new JTextField(20);
        headerPanel.add(searchField);

        String[] searchOptions = {"Search by Name", "Search by ID", "Search by Type"};
        JComboBox<String> searchType = new JComboBox<>(searchOptions);
        headerPanel.add(searchType);

        String[] sortOptions = {"Sort by Name", "Sort by ID", "Sort by Type"};
        JComboBox<String> sortType = new JComboBox<>(sortOptions);
        headerPanel.add(sortType);

        String[] sortSubOptions = {"Asc", "Desc"};
        JComboBox<String> sortSubType = new JComboBox<>(sortSubOptions);
        headerPanel.add(sortSubType);

        JButton searchButton = new JButton("Search");
        headerPanel.add(searchButton);

        // Nút reset
        JButton resetButton = new JButton("Reset");
        headerPanel.add(resetButton);

        loginButton = new JButton("Log In");
        logoutButton = new JButton("Log Out");
        signUpButton = new JButton("Sign Up");
        JButton userInforButton = new JButton("User");
        
        // Thêm các nút vào headerPanel
        headerPanel.add(loginButton);
        headerPanel.add(signUpButton);
        headerPanel.add(logoutButton);
        headerPanel.add(userButton);
        logoutButton.setVisible(false);  // Nếu chưa đăng nhập, ẩn nút "Log Out"
        loginButton.setVisible(true);  // Nếu đã đăng nhập, ẩn nút "Log In"
        signUpButton.setVisible(true);  // Ẩn nút "Sign In" khi đã đăng nhập
        userButton.setVisible(false);  // Ban đầu ẩn đi
        
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
            updateLoginUI();  // Cập nhật lại giao diện sau khi đăng xuất
        });    
   

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

        // Thêm các phần vào cửa sổ
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(productPanel, BorderLayout.CENTER);
        add(paginationPanel, BorderLayout.SOUTH);

        // Xử lý sự kiện tìm kiếm
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
        JDialog signupDialog = new JDialog(this, "Sign Up", true);
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
            JDialog userInfoDialog = new JDialog(this, "User Information", true);  // Tạo dialog
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
            JOptionPane.showMessageDialog(this, "No user is logged in.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showLoginDialog() {
        JDialog loginDialog = new JDialog(this, "Log In", true);
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

    // Khởi tạo và thêm nút "User" vào giao diện
    JButton userButton = new JButton("User");
	
    private void loadProductList() {
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
        String query = "SELECT * FROM item";
        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement stmt = connection.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void displayProducts(List<ProductPanel> products) {
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
        } else if (sortBy.equals("Sort by ID")) {
            sortColumn = "id";  // Sắp xếp theo "id"
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

            stmt.setString(1, "%" + searchText + "%");  // Sử dụng % cho tìm kiếm chứa chuỗi

            ResultSet resultSet = stmt.executeQuery();
            // Xóa các sản phẩm cũ và hiển thị các sản phẩm mới từ kết quả tìm kiếm
            productList.clear();  // Clear current list of products
            while (resultSet.next()) {
                ProductPanel product = ProductPanel.createFromResultSet(resultSet,this);
                productList.add(product);
            }
            page = 1;
            displayProducts(productList);  // Hiển thị lại danh sách sản phẩm đã tìm kiếm và sắp xếp
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainMenu().setVisible(true);
            }
        });
    }
    
    public Person getNowPerson() {
        return nowPerson;
    }
    
}
