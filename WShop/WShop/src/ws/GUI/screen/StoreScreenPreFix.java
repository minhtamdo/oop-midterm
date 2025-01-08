package ws.GUI.screen;

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

import ws.GUI.product.ProductPanel;
import ws.main.DatabaseConnection;
import ws.person.Person;

public class StoreScreenPreFix extends JFrame {
    private int page = 1;  // Biến lưu trang hiện tại
    private List<ProductPanel> productList = new ArrayList<>();
    private JPanel productPanel = new JPanel();
    private Person nowPerson = null;
    private JButton loginButton;
    private JButton logoutButton;
    private JButton signUpButton;
    private JButton userInforButton;
    StoreScreenPreFix() {        
    }
 

    
}
