package ws.GUI.main;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class MainGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Hiển thị dialog cho người dùng chọn giữa khách hàng và quản lý
            String[] options = {"Guest", "Manager"};
            int choice = JOptionPane.showOptionDialog(
                null,  // Component cha (null để hiển thị ở giữa màn hình)
                "Enter as",
                "Select window",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
            );

            if (choice == 0) {
                // Nếu chọn "Khách hàng", mở MainMenu
                MainMenu mainMenu = new MainMenu();
                mainMenu.setVisible(true);
            } else if (choice == 1) {
                ManagerMenu managerMenu = new ManagerMenu();
                managerMenu.setVisible(true);
            }
        });
    }
}
