package ws.GUI.main;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import ws.GUI.screen.CartScreen;
import ws.GUI.screen.FavouriteScreen;
import ws.GUI.screen.StoreScreen;
import ws.person.Person;

public class MainMenu extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Person nowPerson; 
    public MainMenu() {
        setTitle("9S Fashion Store");
        setSize(1050, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Khởi tạo CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Thêm các màn hình vào CardLayout
        mainPanel.add(new StoreScreen(this, this), "StoreScreen");
        mainPanel.add(new CartScreen(this, this), "CartScreen");
        mainPanel.add(new FavouriteScreen(this, this), "FavouriteScreen");

        createMenuBar();

        add(mainPanel);

        cardLayout.show(mainPanel, "StoreScreen");
    }

    public Person getNowPerson() {
		return nowPerson;
	}

	public void setNowPerson(Person nowPerson) {
		this.nowPerson = nowPerson;
	}

	private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");

        JMenuItem screen1Item = new JMenuItem("Store Screen");
        screen1Item.addActionListener(e -> {
        	mainPanel.removeAll();
        	mainPanel.add(new StoreScreen(this, this), "StoreScreen");
            mainPanel.add(new CartScreen(this, this), "CartScreen");
            mainPanel.add(new FavouriteScreen(this, this), "FavouriteScreen");
            mainPanel.revalidate(); // Cập nhật lại bố cục
            mainPanel.repaint();    // Làm mới giao diện
            cardLayout.show(mainPanel, "StoreScreen");
        });

        JMenuItem screen2Item = new JMenuItem("Cart Screen");
        screen2Item.addActionListener(e -> {
        	mainPanel.removeAll();
        	mainPanel.add(new StoreScreen(this, this), "StoreScreen");
            mainPanel.add(new CartScreen(this, this), "CartScreen");
            mainPanel.add(new FavouriteScreen(this, this), "FavouriteScreen");
            mainPanel.revalidate(); // Cập nhật lại bố cục
            mainPanel.repaint();    // Làm mới giao diện
            cardLayout.show(mainPanel, "CartScreen");
        });

        JMenuItem screen3Item = new JMenuItem("Favourite Screen");
        screen3Item.addActionListener(e -> {
        	mainPanel.removeAll();
        	mainPanel.add(new StoreScreen(this, this), "StoreScreen");
            mainPanel.add(new CartScreen(this, this), "CartScreen");
            mainPanel.add(new FavouriteScreen(this, this), "FavouriteScreen");
            mainPanel.revalidate(); // Cập nhật lại bố cục
            mainPanel.repaint();    // Làm mới giao diện
        	cardLayout.show(mainPanel, "FavouriteScreen");
        });

        menu.add(screen1Item);
        menu.add(screen2Item);
        menu.add(screen3Item);

        menuBar.add(menu);
        setJMenuBar(menuBar);
    }
	
    public void updateNowPerson(Person newPerson) {
        this.setNowPerson(newPerson);
        System.out.println("updated");
        if(nowPerson != null) {
        	System.out.println(nowPerson.getEmail());   
        }
    }
    
    
}
