package ws.GUI.main;

import java.awt.CardLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ws.GUI.screen.ManagerStoreScreen;
import ws.main.DatabaseConnection;
import ws.person.Person;

public class ManagerMenu extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Person nowPerson;
    private ManagerMenu mMenu;
    public ManagerMenu() {
        // Khởi tạo cửa sổ
        setTitle("9S Fashion Store for Manager");
        setSize(1050, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Hiển thị dialog đăng nhập
        showLoginDialog(this);
    }

    // Hàm hiển thị dialog đăng nhập
    private void showLoginDialog(ManagerMenu mm) {
        mMenu = mm;
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

            // Kiểm tra đăng nhập từ cơ sở dữ liệu
            if (checkLogin(username, password)) {
                JOptionPane.showMessageDialog(loginDialog, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loginDialog.dispose();  // Đóng dialog đăng nhập
                initializeManagerMenu();  // Khởi tạo menu quản lý
            } else {
            	this.dispose();
                JOptionPane.showMessageDialog(loginDialog, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                mMenu.dispose();
            }
        });

        loginDialog.pack();  // Sắp xếp lại giao diện
        loginDialog.setVisible(true);  // Hiển thị dialog
    }

    // Kiểm tra thông tin đăng nhập từ cơ sở dữ liệu
    private boolean checkLogin(String username, String password) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM person WHERE username = ? AND password = ? AND role = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, "Manager");

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
                return true;  // Đăng nhập thành công
            } else {
            	
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while connecting to the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;  // Đăng nhập thất bại
    }

    // Khởi tạo menu quản lý sau khi đăng nhập thành công
    private void initializeManagerMenu() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Thêm các màn hình vào CardLayout
        mainPanel.add(new ManagerStoreScreen(this, this), "StoreScreen");

        add(mainPanel);
        cardLayout.show(mainPanel, "StoreScreen");
    }

    public Person getNowPerson() {
        return nowPerson;
    }

    public void setNowPerson(Person nowPerson) {
        this.nowPerson = nowPerson;
    }

    public void updateNowPerson(Person newPerson) {
        this.setNowPerson(newPerson);
        System.out.println("updated");
        if (nowPerson != null) {
            System.out.println(nowPerson.getEmail());
        }
    }
}
