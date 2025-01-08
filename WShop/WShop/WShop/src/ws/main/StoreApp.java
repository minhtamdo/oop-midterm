package ws.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StoreApp {
    private JFrame frame;
    private JPanel mainPanel;
    private JTextArea displayArea;
    private JTextField searchField;
    private JButton searchByIdButton, searchByTitleButton, searchByTypeButton;

    public StoreApp() {
        frame = new JFrame("Store Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 680);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Display Area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        searchById("");
        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchField = new JTextField(20);
        searchByIdButton = new JButton("Search by ID");
        searchByTitleButton = new JButton("Search by Title");
        searchByTypeButton = new JButton("Search by Type");

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchByIdButton);
        searchPanel.add(searchByTitleButton);
        searchPanel.add(searchByTypeButton);

        // Add components to main panel
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // Add main panel to frame
        frame.add(mainPanel);

        // Button actions
        searchByIdButton.addActionListener(e -> searchById(searchField.getText()));
        searchByTitleButton.addActionListener(e -> searchByTitle(searchField.getText()));
        searchByTypeButton.addActionListener(e -> searchByType(searchField.getText()));

        frame.setVisible(true);
    }

    private void searchById(String id) {
        String sql = "SELECT * FROM item WHERE id LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + id + "%");
            ResultSet rs = stmt.executeQuery();
            displayArea.setText("Results:\n");

            while (rs.next()) {
                displayArea.append(
                        "ID: " + rs.getString("id") + ", " +
                        "Title: " + rs.getString("title") + ", " +
                        "Price: " + rs.getFloat("price") + ", " +
                        "Type: " + rs.getString("type") + ", " +
                        "Material: " + rs.getString("material") + ", " +
                        "Subtype: " + rs.getString("subtype") + ", " +
                        "Size: " + rs.getInt("size") + "\n"
                );
            }
        } catch (SQLException e) {
            displayArea.setText("Error: " + e.getMessage());
        }
    }

    private void searchByTitle(String title) {
        String sql = "SELECT * FROM item WHERE title LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + title + "%");
            ResultSet rs = stmt.executeQuery();
            displayArea.setText("Results:\n");

            while (rs.next()) {
                displayArea.append(
                        "ID: " + rs.getString("id") + ", " +
                        "Title: " + rs.getString("title") + ", " +
                        "Price: " + rs.getFloat("price") + ", " +
                        "Type: " + rs.getString("type") + ", " +
                        "Material: " + rs.getString("material") + ", " +
                        "Subtype: " + rs.getString("subtype") + ", " +
                        "Size: " + rs.getInt("size") + "\n"
                );
            }
        } catch (SQLException e) {
            displayArea.setText("Error: " + e.getMessage());
        }
    }

    private void searchByType(String type) {
        String sql = "SELECT * FROM item WHERE type LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + type + "%");
            ResultSet rs = stmt.executeQuery();
            displayArea.setText("Results:\n");

            while (rs.next()) {
                displayArea.append(
                        "ID: " + rs.getString("id") + ", " +
                        "Title: " + rs.getString("title") + ", " +
                        "Price: " + rs.getFloat("price") + ", " +
                        "Type: " + rs.getString("type") + ", " +
                        "Material: " + rs.getString("material") + ", " +
                        "Subtype: " + rs.getString("subtype") + ", " +
                        "Size: " + rs.getInt("size") + "\n"
                );
            }
        } catch (SQLException e) {
            displayArea.setText("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new StoreApp();
    }
}
