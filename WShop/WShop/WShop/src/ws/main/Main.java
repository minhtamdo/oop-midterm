package ws.main;

import java.util.ArrayList;
import java.util.Scanner;

import ws.Manager.OrderList;
import ws.cart.Cart;
import ws.favourite.Favourite;
import ws.item.Item;
import ws.item.accessories.Bag;
import ws.item.accessories.Glasses;
import ws.item.accessories.Hat;
import ws.item.footwear.Shoe;
import ws.item.footwear.Sock;
import ws.item.outerwear.Coat;
import ws.item.pants.Jean;
import ws.item.pants.Jogger;
import ws.item.pants.Trouser;
import ws.item.top.Croptop;
import ws.item.top.Hoodie;
import ws.item.top.Shirt;
import ws.item.top.Sweater;
import ws.item.top.Tshirt;
import ws.person.Person;
import ws.store.Store;

public class Main {
	public static ArrayList<Person> personList = new ArrayList<Person>();
	public static Person nowPerson;
	public static Store store = new Store();
	public static Cart cart = new Cart();
	public static Favourite favourite = new Favourite();
	public static Scanner scanner = new Scanner(System.in);
    public static OrderList orderList = new OrderList();
    public static int nbOrder;
	
    public static void main(String[] args) {
		testcase();
		nowPerson= new Person(1,"johndoe", "password123", "John Doe", "john.doe@example.com","313213", "Manager");
		space();
		space();
		showMenu();
	}
	public static void space() {
		for(int i=0;i<30;i++) {
			System.out.println();
		}
	}
	public static void showMenu() {
		int choice;
		do {
			System.out.println("******************* Main menu *******************");
			System.out.println("1. View Shop");
			System.out.println("2. View Cart");
			System.out.println("3. View Favourite");
			if(nowPerson == null) {
				System.out.println("4. Log in");
				System.out.println("5. Sign in");				
			} else {
				System.out.println("4. Log out");
				if(nowPerson.getRole().equals("Manager")) {
					System.out.println("5. Manager Menu");					
				}
			}
			System.out.println("0. Exit");
			System.out.println("*************************************************");
			System.out.print("		Your order: ");
			choice = scanner.nextInt();
            scanner.nextLine(); 
            switch(choice) {
	        	case 1:
	        		viewShop();
	        		break;
	        	case 2:
	        		viewCart();
	        		break;
	        	case 3:
	        		viewFavourite();
	        		break;
	        	case 4:
	        		if(nowPerson == null) {
		        		login();	
	        		} else {
	        			logout();
	        		}
	        		break;
	        	case 5:
	        		if(nowPerson == null) {
	        			signin();	
	        		} else {
	        			if(nowPerson.getRole().equals("Manager")) {
	        				managerMenu();
	        			} else 
	        				System.out.println("Invalid choice");
	        		}
	        		break;
	        	case 0:
	        		System.out.println("Exited");
	        		System.exit(0);
	        		break;
	        	default:
	        		System.out.println("Invalid choice");
	        		break;
            }
            			
		} while(choice > 0);
	}
	public static void viewShop() {
		int choice;
		store.print();
		do{
			System.out.println("1. Sort");
			System.out.println("2. Filter");
			System.out.println("3. Add to Cart");
			System.out.println("4. Add to Favourite");
			System.out.println("0. Back");
			System.out.println("*************************************************");
			System.out.print("		Your order: ");
			choice = scanner.nextInt();
			scanner.nextLine();
			switch (choice) {
			case 1:
				sort(1);
				break;
			case 2:
				filter(1);
				break;
			case 3:
				addToYourCart();
				break;
			case 4:
				addToYourFavourite();
				break;
			case 0:
				break;
			default:
				System.out.println("Invalid choice");
			}
		} while (choice!=0);	
	}
	public static void viewCart() {
		int choice;
		if(nowPerson != null) {
			cart.print(nowPerson.getEmail());
			do{
				System.out.println("1. Sort");
				System.out.println("2. Filter");
				System.out.println("3. Add to your Favourite");
				System.out.println("4. Change the quantity");
				System.out.println("5. Remove item");
				System.out.println("6. Place order");
				System.out.println("0. Back");
				System.out.println("*************************************************");
				System.out.print("		Your order: ");
				choice = scanner.nextInt();
				scanner.nextLine();
				switch (choice) {
				case 1:
					sort(2);
					break;
				case 2:
					filter(2);
					break;
				case 3:
					addToYourFavourite();
					break;
				case 4:
					changeTheQuantity();
					break;
				case 5:
					removeItemCart();
					break;
				case 6:
					placeOrder();
					break;
				default:
					System.out.println("Invalid choice");
				}
			} while (choice!=0);
			
		} else {
			System.out.println("Please log in to see your Cart");
			login();
		}
	}
	public static void viewFavourite() {
		int choice;
		if(nowPerson != null) {
			favourite.print(nowPerson.getEmail());
			do{
				System.out.println("1. Sort");
				System.out.println("2. Filter");
				System.out.println("3. Add to your Cart");
				System.out.println("4. Remove from Favourite");
				System.out.println("0. Back");
				System.out.println("*************************************************");
				System.out.print("		Your order: ");
				choice = scanner.nextInt();
				scanner.nextLine();
				switch (choice) {
				case 0:
					break;
				case 1:
					sort(3);
					break;
				case 2:
					filter(3);
					break;
				case 3:
					addToYourCart();
					break;
				case 4:
					removeItemFavourite();
					break;
				default:
					System.out.println("Invalid choice");
				}
			} while (choice!=0);
			
		} else {
			System.out.println("Please log in to see your Favourite");
			login();
		}			
	}
	public static void addToYourCart() {
		if(nowPerson == null){
			System.out.println("Please log in to see your Cart");
			login();
		} else {
			System.out.print("Enter the Item's id: ");
			String id = scanner.nextLine();
			cart.addItem(nowPerson.getEmail(), id);
		}
	}
	public static void addToYourFavourite() {
		if(nowPerson == null){
			System.out.println("Please log in to see your Favourite");
			login();
		} else {
			System.out.print("Enter the Item's id: ");
			String id = scanner.nextLine();
			favourite.addItem(nowPerson.getEmail(), id);			
		}
	}
	public static void login() {
		String tk,mk;
		System.out.println("******************** Log in *********************");
		System.out.print("Account/Email: ");
		tk = scanner.nextLine();
		System.out.print("Password: ");
		mk = scanner.nextLine();
		for (Person person : personList) {
			if(person.getUsername().equals(tk) && person.getPassword().equals(mk) || person.getEmail().equals(tk) && person.getPassword().equals(mk)) {
				nowPerson = person;
				break;
			}
		}
		if (nowPerson != null) {
			System.out.println("Log in successfully ");
			System.out.println("Hello " + nowPerson.getName());
		} else {
			System.out.println("Account is not found");
		}
		
	}
	public static void signin() {
		boolean check = true;
		System.out.println("******************** Sign in ********************");
		System.out.println("Name: ");
		String name = scanner.nextLine();
		System.out.println("Email: ");
		String email = scanner.nextLine();
		System.out.println("Account: ");
		String account = scanner.nextLine();
		System.out.println("Password: ");
		String password = scanner.nextLine();
		System.out.println("Role: ");
		String role = scanner.nextLine();
		for (Person person : personList) {
			if(person.getEmail().equals(email)) {
				System.out.println("Email has alreadyy been used");
				check = false;
				break;
			}  
		}
		if (check==true) {
			nowPerson = new Person(1,name,email,account,password,"012313",role);
			personList.add(nowPerson);
			System.out.println("Sign in successfully. Welcome " + name);
		}
	}
	public static void logout() {
		System.out.println("Log out successfully ");
		System.out.println("See you again " + nowPerson.getName());
		nowPerson = null;	
	}
	public static void sort(int list) {
		int type = 0;
		do {
			System.out.println("1. Sort by title");
			System.out.println("2. Sort by price");
			System.out.println("3. Sort by type");
			System.out.println("*************************************************");
			System.out.print("		Your order: ");
			type = scanner.nextInt();
			scanner.nextLine();
			switch(type) {
				case 1:
					sort(list,1);
					break;
				case 2:
					sort(list,2);
					break;
				case 3:
					sort(list,3);
					break;
				default:
					System.out.println("Invalid choice");
			}
		} while (type != 1 && type != 2 && type != 3);
	}
	public static void placeOrder() {
		System.out.print("Enter your phone number: ");		
		String pnum = scanner.nextLine();
		System.out.print("Enter our address: ");
		String address = scanner.nextLine();		
		System.out.println("You want to place order");
		System.out.println("Yes");
		System.out.println("No");
		String choice = scanner.nextLine();
		switch (choice) {
		case "Yes":
			cart.placeOrder(nowPerson.getEmail(), address, pnum);
			break;
		case "No":
			break;	
		default:
		}
	}
	public static void changeTheQuantity(){
		System.out.print("Enter the Item's id: ");
		String id = scanner.nextLine();
		System.out.print("Enter the new quantity: ");
		int qty = scanner.nextInt();
		scanner.nextLine();
		if (qty <=0) {
			cart.removeItem(nowPerson.getEmail(), id);
			return;
		}
		cart.changeQuantity(nowPerson.getEmail(), id, qty);
	}		
	public static void removeItemCart(){
		System.out.print("Enter the Item's id: ");
		String id = scanner.nextLine();
		cart.removeItem(nowPerson.getEmail(), id);
	}
	public static void removeItemFavourite() {
		System.out.print("Enter the Item's id: ");
		String id = scanner.nextLine();
		favourite.removeItem(nowPerson.getEmail(), id);
	}
	public static void sort(int list, int type) {
		int choice = 0;
		Store storeForSort = store;
		Cart cartForSort = cart;
		Favourite favouriteForSort = favourite;
		
		do {
			System.out.println("1. Increase");
			System.out.println("2. Decrease");
			System.out.println("*************************************************");
			System.out.print("		Your order: ");
			choice = scanner.nextInt();
			scanner.nextLine();
			switch(choice) { //in1 de2
			case 1:
				switch(type) { //title1, price2, type3
				case 1:
					switch(list) { //storeForSort1, cartForSort2, favouriteForSort3
					case 1:
						storeForSort.sortByTitle("ASC");
						break;
					case 2:
						cartForSort.sortCartByProductName(nowPerson.getEmail(), "ASC");
						break;
					case 3:
						favouriteForSort.sortFavouriteByTitle(nowPerson.getEmail(),"ASC");
					}
					break;
				case 2:
					switch(list) { //storeForSort1, cartForSort2, favouriteForSort3
					case 1:
						storeForSort.sortByPrice("ASC");
						break;
					case 2:
						cartForSort.sortCartByPrice(nowPerson.getEmail(), "ASC");
						break;
					case 3:
						favouriteForSort.sortFavouriteByPrice(nowPerson.getEmail(),"ASC");
					}
					break;
				case 3:
					switch(list) { //storeForSort1, cartForSort2, favouriteForSort3
					case 1:
						storeForSort.sortByType("ASC");
						break;
					case 2:
						cartForSort.sortCartByType(nowPerson.getEmail(), "ASC");
						break;
					case 3:
						favouriteForSort.sortFavouriteByType(nowPerson.getEmail(),"ASC");
					}
					break;
				}
				break; 
			case 2:
				switch(type) { //title1, price2, type3
				case 1:
					switch(list) { //storeForSort1, cartForSort2, favouriteForSort3
					case 1:
						storeForSort.sortByTitle("DESC");
						break;
					case 2:
						cartForSort.sortCartByProductName(nowPerson.getEmail(), "DESC");
						break;
					case 3:
						favouriteForSort.sortFavouriteByTitle(nowPerson.getEmail(),"DESC");
					}
					break;
				case 2:
					switch(list) { //storeForSort1, cartForSort2, favouriteForSort3
					case 1:
						storeForSort.sortByPrice("DESC");
						break;
					case 2:
						cartForSort.sortCartByPrice(nowPerson.getEmail(), "DESC");
						break;
					case 3:
						favouriteForSort.sortFavouriteByPrice(nowPerson.getEmail(),"DESC");
					}
					break;
				case 3:
					switch(list) { //storeForSort1, cartForSort2, favouriteForSort3
					case 1:
						storeForSort.sortByType("DESC");
						break;
					case 2:
						cartForSort.sortCartByType(nowPerson.getEmail(), "DESC");
						break;
					case 3:
						favouriteForSort.sortFavouriteByType(nowPerson.getEmail(),"DESC");
						break;
					}
					break;
				}
				break;
			default:
				System.out.println("Invalid choice");
			}
		} while (choice != 1 && choice != 2);		
	}
	public static void filter(int list) {
		ArrayList<Item> searchList = new ArrayList();
		int choice = 0;
		System.out.println("1. Filter by Title");
		System.out.println("2. Filter by ID");
		System.out.println("3. Filter by Type");
		System.out.println("*************************************************");
		System.out.print("		Your order: ");
		choice = scanner.nextInt();
		scanner.nextLine();
		System.out.print("Search for: ");
		String key = scanner.nextLine();
		switch(list) {
		case 1: //Store
			switch(choice) {
			case 1:
				store.searchByTitle(key);
				break;
			case 2:
				store.searchById(key);
				break;
			case 3:
				store.searchByType(key);
				break;
			}
			break;
		case 2:
			switch(choice) {
			case 1:
				cart.searchByTitle(nowPerson.getEmail(), key);
				break;
			case 2:
				cart.searchById(nowPerson.getEmail(), key);
				break;
			case 3:
				cart.searchByType(nowPerson.getEmail(), key);
				break;
			}
			break;
		case 3:
			switch(choice) {
			case 1:
				favourite.searchByTitle(nowPerson.getEmail(), key);
				break;
			case 2:
				favourite.searchById(nowPerson.getEmail(), key);
				break;
			case 3:
				favourite.searchByType(nowPerson.getEmail(), key);
				break;
			}
		}
	}
	public static void printFilterStore(ArrayList<Item> searchList) {
    	int i = 0;
        System.out.println("********************** 9S Store ***********************");
        System.out.println(String.format("%-4s %-10s %-20s %-10s %-15s %-15s %-10s %-10s",
    	        "Num", "ID", "Title", "Price", "Type", "Subtype", "Size", "Material"));
        for (Item item : searchList) {
        	System.out.println(String.format("%03d. %s", ++i, item.toString()));
        }
        System.out.println("*******************************************************");
    }
	public static void printFilterCart(ArrayList<Item> searchList) {
	   	int i = 0;
	   	float costTotal = 0;
        System.out.println("********************* Your Cart ***********************");
        System.out.println(String.format("%-4s %-10s %-20s %-10s %-15s %-15s %-10s %-10s %-8s",
    	        "Num", "ID", "Title", "Price", "Type", "Subtype", "Size", "Material", "Quantity"));
        for (Item item : searchList) { 
        	System.out.println(String.format("%-3d. %-1s %-1d", ++i, item.toString(), item.getQuantity() ));  
        	costTotal += item.getPrice() * item.getQuantity();
        }
        System.out.println("    Cost: " + costTotal);
        System.out.println("*******************************************************");
    }

	public static void managerMenu(){
		int choice;
		do {
			System.out.println("******************* Manager menu *******************");
			System.out.println("1. Add new Item");
			System.out.println("2. Remove Item");
			System.out.println("0. Exit");
			System.out.println("*************************************************");
			System.out.print("		Your order: ");
			choice = scanner.nextInt();
            scanner.nextLine(); 
            switch(choice) {
	        	case 1:
	        		addNewItem();
	        		break;
	        	case 2:
	        		removeItemFromStore();
	        		break;
	        	case 0: 
	        		break;
	        	default:
	        		System.out.println("Invalid choice");
	        		break;
            }
            			
		} while(choice != 0);
	}
	public static void addNewItem() {
	    System.out.println("******************** Add Item ********************");
	    System.out.println("ID: ");
	    String id = scanner.nextLine();
	    
	    System.out.println("Title: ");
	    String title = scanner.nextLine();
	    System.out.println("Price: ");
	    float price = scanner.nextFloat();
	    scanner.nextLine(); // Clear the buffer
	    System.out.println("Type: ");
	    String type = scanner.nextLine();
	    System.out.println("Material: ");
	    String material = scanner.nextLine();

	    String subtype = null; // Default value for subtype
	    int size = 0; // Default value for size

	    // Kiểm tra loại sản phẩm và lấy các tham số phụ trợ
	    switch (type) {
	        case "Bag":
	        case "Shoe":
	        case "Coat":
	        case "Jean":
	        case "Trouser":
	        case "Croptop":
	        case "Hoodie":
	        case "Shirt":
	        case "Tshirt":
	        case "Sweater":
	            System.out.println("Size: ");
	            size = scanner.nextInt();
	            scanner.nextLine(); // Clear the buffer
	            System.out.println("Subtype: ");
	            subtype = scanner.nextLine();
	            break;
	        case "Glasses":
	        case "Hat":
	        case "Sock":
	            System.out.println("Subtype: ");
	            subtype = scanner.nextLine();
	            break;
	        case "Jogger":
	            System.out.println("Size: ");
	            size = scanner.nextInt();
	            scanner.nextLine(); // Clear the buffer
	            break;
	        default:
	            System.out.println("Item's Type is undefined.");
	            return;
	    }
	    
	    // Tạo đối tượng Item chung và thêm vào store
	    Item item = new Item(id, title, price, type, material);
	    store.addItem(item, subtype, size);
	    System.out.println(item.getTitle() + " has been added to the store.");
	}

	public static void removeItemFromStore() {
		System.out.print("Enter the Item's id: ");
		String id = scanner.nextLine();
		store.removeItem(id);
	}
	public static void testcase() {
		// Test cases for Person
		personList.add(new Person(1,"john123", "password", "John", "john@gmail.com", "99740071", "Guest"));
		personList.add(new Person(2,"anna456", "password", "Anna", "anna@gmail.com", "000000000", "Manager"));
		personList.add(new Person(3,"mark789", "password", "Mark", "mark@gmail.com", "023000000", "Guest"));
		personList.add(new Person(4,"johndoe", "password123", "John Doe", "john.doe@example.com", "0000012330", "Manager"));
		// Test cases for Bags
		
	}
}
