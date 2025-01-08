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
			cart.print();
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
			favourite.print();
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
				case 1:
					sort(3);
					break;
				case 2:
					filter(3);
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
			Item item = store.searchOneById(id);
			if(item==null) {
				System.out.println("Item is not found");
			} else {
				Item item2 = cart.searchOneById(id);
				if(item2!=null) {
					System.out.println("Item is already in your Cart");
				} else {
					cart.addItem(item);
				}
			}
		}
	}
	public static void addToYourFavourite() {
		if(nowPerson == null){
			System.out.println("Please log in to see your Favourite");
			login();
		} else {
			System.out.print("Enter the Item's id: ");
			String id = scanner.nextLine();
			Item item = store.searchOneById(id);
			if(item==null) {
				System.out.println("Item is not found");
			} else {
				Item item2 = favourite.searchOneById(id);
				if(item2!=null) {
					System.out.println("Item is already in your Favourite");
				} else {
					favourite.addItem(item);
				}
			}
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
			if(person.getTk().equals(tk) && person.getMk().equals(mk) || person.getEmail().equals(tk) && person.getMk().equals(mk)) {
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
			nowPerson = new Person(name,email,account,password,role);
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
			cart.placeOrder();
			orderList.addOrder(++nbOrder, nowPerson.getEmail(), pnum, address);
			break;
		case "No":
			break;	
		default:
		}
		
	}
	public static void changeTheQuantity(){
		System.out.print("Enter the Item's id: ");
		String id = scanner.nextLine();
		Item item = cart.searchOneById(id);
		if(item == null) {
			System.out.println("Item is not found");
			return;
		}
		System.out.print("Enter the new quantity: ");
		int qty = scanner.nextInt();
		scanner.nextLine();
		cart.changeQty(id, qty);
	}		
	public static void removeItemCart(){
		System.out.print("Enter the Item's id: ");
		String id = scanner.nextLine();
		Item item = cart.searchOneById(id);
		if(item == null) {
			System.out.println("Item is not found");
		} else {
			cart.removeItem(item.getId());
			System.out.println("Item has been removed");
		}
	}
	public static void removeItemFavourite() {
		System.out.print("Enter the Item's id: ");
		String id = scanner.nextLine();
		Item item = favourite.searchOneById(id);
		if(item == null) {
			System.out.println("Item is not found");
		} else {
			favourite.removeItem(item.getId());
			System.out.println("Item has been removed");
		}
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
						storeForSort.sortByTitleIncrease();
						storeForSort.print();
						break;
					case 2:
						cartForSort.sortByTitleIncrease();
						cartForSort.print();
						break;
					case 3:
						favouriteForSort.sortByTitleIncrease();
						favouriteForSort.print();
					}
					break;
				case 2:
					switch(list) { //storeForSort1, cartForSort2, favouriteForSort3
					case 1:
						storeForSort.sortByPriceIncrease();
						storeForSort.print();
						break;
					case 2:
						cartForSort.sortByPriceIncrease();
						cartForSort.print();
						break;
					case 3:
						favouriteForSort.sortByPriceIncrease();
						favouriteForSort.print();
					}
					break;
				case 3:
					switch(list) { //storeForSort1, cartForSort2, favouriteForSort3
					case 1:
						storeForSort.sortByTypeIncrease();
						storeForSort.print();
						break;
					case 2:
						cartForSort.sortByTypeIncrease();
						cartForSort.print();
						break;
					case 3:
						favouriteForSort.sortByTypeIncrease();
						favouriteForSort.print();
					}
					break;
				}
				break; 
			case 2:
				switch(type) { //title1, price2, type3
				case 1:
					switch(list) { //storeForSort1, cartForSort2, favouriteForSort3
					case 1:
						storeForSort.sortByTitleDecrease();
						storeForSort.print();
						break;
					case 2:
						cartForSort.sortByTitleDecrease();
						cartForSort.print();
						break;
					case 3:
						favouriteForSort.sortByTitleDecrease();
						favouriteForSort.print();
					}
					break;
				case 2:
					switch(list) { //storeForSort1, cartForSort2, favouriteForSort3
					case 1:
						storeForSort.sortByPriceDecrease();
						storeForSort.print();
						break;
					case 2:
						cartForSort.sortByPriceDecrease();
						cartForSort.print();
						break;
					case 3:
						favouriteForSort.sortByPriceDecrease();
						favouriteForSort.print();
					}
					break;
				case 3:
					switch(list) { //storeForSort1, cartForSort2, favouriteForSort3
					case 1:
						storeForSort.sortByTypeDecrease();
						storeForSort.print();
						break;
					case 2:
						cartForSort.sortByTypeDecrease();
						cartForSort.print();
						break;
					case 3:
						favouriteForSort.sortByTypeDecrease();
						favouriteForSort.print();
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
				searchList = store.searchByTitle(key);
				printFilterStore(searchList);
				break;
			case 2:
				searchList = store.searchById(key);
				printFilterStore(searchList);
				break;
			case 3:
				searchList = store.searchByType(key);
				printFilterStore(searchList);
				break;
			}
			break;
		case 2:
			switch(choice) {
			case 1:
				searchList = cart.searchByTitle(key);
				printFilterCart(searchList);
				break;
			case 2:
				searchList = cart.searchById(key);
				printFilterCart(searchList);
				break;
			case 3:
				searchList = cart.searchByType(key);
				printFilterCart(searchList);
				break;
			}
			break;
		case 3:
			switch(choice) {
			case 1:
				searchList = favourite.searchByTitle(key);
				printFilterStore(searchList);
				break;
			case 2:
				printFilterStore(searchList);
				searchList = favourite.searchById(key);
				break;
			case 3:
				searchList = favourite.searchByType(key);
				printFilterStore(searchList);
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
		for (Item item : store.getItemsInStore()) {
			if(item.getId().equals(id)) {
				System.out.println("Item is already in Store");
				return;
			}  
		}
		System.out.println("Title: ");
		String title = scanner.nextLine();
		System.out.println("Price: ");
		float price = scanner.nextFloat();
		scanner.nextLine();
		System.out.println("Type: ");
		String type = scanner.nextLine();
		System.out.println("Material: ");
		String material = scanner.nextLine();
		
		if(type.equals("Bag")) {
			System.out.println("Size: ");
			int size = scanner.nextInt();
			scanner.nextLine();
			System.out.println("Subtype: ");
			String subType = scanner.nextLine();
			store.addItem(new Bag(id,title,price,type, material, subType,size));
		} else
		if(type.equals("Glasses")) {
			System.out.println("Subtype: ");
			String subType = scanner.nextLine();
			store.addItem(new Glasses(id,title,price,type, material, subType));
		} else
		if(type.equals("Hat")) {
			System.out.println("Subtype: ");
			String subType = scanner.nextLine();
			store.addItem(new Hat(id,title,price,type, material, subType));
		} else
		if(type.equals("Shoe")) {
			System.out.println("Size: ");
			int size = scanner.nextInt();
			scanner.nextLine();
			System.out.println("Subtype: ");
			String subType = scanner.nextLine();
			store.addItem(new Shoe(id,title,price,type, material, subType,size));
		} else
		if(type.equals("Sock")) {
			System.out.println("Subtype: ");
			String subType = scanner.nextLine();
			store.addItem(new Sock(id,title,price,type, material, subType));
		} else
		if(type.equals("Coat")) {
			System.out.println("Size: ");
			int size = scanner.nextInt();
			scanner.nextLine();
			System.out.println("Subtype: ");
			String subType = scanner.nextLine();
			store.addItem(new Coat(id,title,price,type, material, subType,size));
		} else
		if(type.equals("Jean")) {
			System.out.println("Size: ");
			int size = scanner.nextInt();
			scanner.nextLine();
			System.out.println("Subtype: ");
			String subType = scanner.nextLine();
			store.addItem(new Jean(id,title,price,type, material, subType,size));
		} else
		if(type.equals("Jogger")) {
			System.out.println("Size: ");
			int size = scanner.nextInt();
			scanner.nextLine();
			store.addItem(new Jogger(id,title,price,type, material,size));
		} else
		if(type.equals("Trouser")) {
			System.out.println("Size: ");
			int size = scanner.nextInt();
			scanner.nextLine();
			System.out.println("Subtype: ");
			String subType = scanner.nextLine();
			store.addItem(new Trouser(id,title,price,type, material, subType,size));
		} else
		if(type.equals("Croptop")) {
			System.out.println("Size: ");
			int size = scanner.nextInt();
			scanner.nextLine();
			System.out.println("Subtype: ");
			String subType = scanner.nextLine();
			store.addItem(new Croptop(id,title,price,type, material, subType,size));
		} else
		if(type.equals("Hoodie")) {
			System.out.println("Size: ");
			int size = scanner.nextInt();
			scanner.nextLine();
			System.out.println("Subtype: ");
			String subType = scanner.nextLine();
			store.addItem(new Hoodie(id,title,price,type, material, subType,size));
		} else
		if(type.equals("Shirt")) {
			System.out.println("Size: ");
			int size = scanner.nextInt();
			scanner.nextLine();
			System.out.println("Subtype: ");
			String subType = scanner.nextLine();
			store.addItem(new Shirt(id,title,price,type, material, subType,size));
		} else
		if(type.equals("Sweater")) {
			System.out.println("Size: ");
			int size = scanner.nextInt();
			scanner.nextLine();
			store.addItem(new Sweater(id,title,price,type, material,size));
		} else
		if(type.equals("Tshirt")) {
			System.out.println("Size: ");
			int size = scanner.nextInt();
			scanner.nextLine();
			System.out.println("Subtype: ");
			String subType = scanner.nextLine();
			store.addItem(new Bag(id,title,price,type, material, subType,size));
		} else {
			System.out.println("Item's Type is undefinded");
			return;
		}		
	}
	public static void removeItemFromStore() {
		System.out.print("Enter the Item's id: ");
		String id = scanner.nextLine();
		Item item = store.searchOneById(id);
		if(item == null) {
			System.out.println("Item is not found");
		} else {
			store.removeItem(item.getId());
			System.out.println("Item has been removed");
		}	
	}
	public static void testcase() {
		// Test cases for Person
		personList.add(new Person("john123", "password", "John", "john@gmail.com", "Guest"));
		personList.add(new Person("anna456", "password", "Anna", "anna@gmail.com", "Manager"));
		personList.add(new Person("mark789", "password", "Mark", "mark@gmail.com", "Guest"));
		// Test cases for Bags
		store.addItem(new Bag("Ba1", "City Explorer", 49.99f, "Bag", "Leather", "basic", 25));
		store.addItem(new Bag("Ba2", "Urban Adventure", 59.99f, "Bag", "Canvas", "basic", 30));
		store.addItem(new Bag("Ba3", "Weekend Escape", 39.99f, "Bag", "Synthetic", "basic", 20));
		// Test cases for Glasses
		store.addItem(new Glasses("Gl1", "Sunshine", 19.99f, "Glasses", "Plastic", "basic"));
		store.addItem(new Glasses("Gl2", "Vision Pro", 29.99f, "Glasses", "Metal", "basic"));
		// Test cases for Hats
		store.addItem(new Hat("Ha1", "Beanie", 15.99f, "Hat", "Wool", "basic"));
		store.addItem(new Hat("Ha2", "Snapback", 25.99f, "Hat", "Cotton", "basic"));
		// Test cases for Shoes
		store.addItem(new Shoe("Sh1", "Trail Runner", 89.99f, "Shoe", "Mesh", "basic", 42));
		store.addItem(new Shoe("Sh2", "Urban Sneakers", 79.99f, "Shoe", "Synthetic", "basic", 40));
		// Test cases for Socks
		store.addItem(new Sock("So1", "Ankle Fit", 4.99f, "Sock", "Cotton", "basic"));
		store.addItem(new Sock("So2", "Sporty", 6.99f, "Sock", "Polyester", "basic"));
		// Test cases for Coats
		store.addItem(new Coat("Co1", "Winter Shield", 129.99f, "Coat", "Wool", "basic", 48));
		store.addItem(new Coat("Co2", "Rain Defender", 99.99f, "Coat", "Nylon", "basic", 46));
		// Test cases for Jeans
		store.addItem(new Jean("Je1", "Slim Fit", 49.99f, "Jean", "Denim", "basic", 32));
		store.addItem(new Jean("Je2", "Classic Blue", 59.99f, "Jean", "Denim", "basic", 34));
		// Test cases for Joggers
		store.addItem(new Jogger("Jo1", "Comfy Jogger", 39.99f, "Jogger", "Cotton", 30));
		store.addItem(new Jogger("Jo2", "Sport Jogger", 44.99f, "Jogger", "Polyester", 32));
		// Test cases for Trousers
		store.addItem(new Trouser("Tr1", "Business Formal", 69.99f, "Trouser", "Wool", "basic", 32));
		store.addItem(new Trouser("Tr2", "Casual Fit", 49.99f, "Trouser", "Cotton", "basic", 34));
		// Test cases for Croptops
		store.addItem(new Croptop("Cr1", "Summer Breeze", 29.99f, "Croptop", "Cotton", "basic", 28));
		store.addItem(new Croptop("Cr2", "Chic Style", 34.99f, "Croptop", "Polyester", "basic", 30));
		// Test cases for Hoodies
		store.addItem(new Hoodie("Ho1", "Cozy Hoodie", 59.99f, "Hoodie", "Cotton", "basic", 40));
		store.addItem(new Hoodie("Ho2", "Street Style", 69.99f, "Hoodie", "Polyester", "basic", 42));
		// Test cases for Shirts
		store.addItem(new Shirt("Sh1", "Oxford Shirt", 49.99f, "Shirt", "Cotton", "basic", 38));
		store.addItem(new Shirt("Sh2", "Casual Shirt", 39.99f, "Shirt", "Linen", "basic", 40));
		// Test cases for Sweaters
		store.addItem(new Sweater("Sw1", "Classic Knit", 59.99f, "Sweater", "Wool", 38));
		store.addItem(new Sweater("Sw2", "Modern Pullover", 49.99f, "Sweater", "Cotton", 40));
		// Test cases for Tshirts
		store.addItem(new Tshirt("Ts1", "Graphic Tee", 19.99f, "Tshirt", "Cotton", "basic", 38));
		store.addItem(new Tshirt("Ts2", "Plain White", 14.99f, "Tshirt", "Cotton", "basic", 40));

	}
}
