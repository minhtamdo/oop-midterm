package ws.cart;

import java.util.ArrayList;
import java.util.Collections;

import ws.item.Item;
import ws.item.ItemComparatorByPriceDecrease;
import ws.item.ItemComparatorByPriceIncrease;
import ws.item.ItemComparatorByTitleDecrease;
import ws.item.ItemComparatorByTitleIncrease;
import ws.item.ItemComparatorByTypeDecrease;
import ws.item.ItemComparatorByTypeIncrease;

public class Cart {
	private ArrayList<Item> itemsInCart = new ArrayList<>();
	private ArrayList<Item> searchItems = new ArrayList<>();
	public void placeOrder() {
    	if(itemsInCart.isEmpty()) {
    		System.out.println("The cart is empty.");
    	}
    	else {
    		itemsInCart.clear();
    		System.out.println("Order has been placed.");
    	}
    }
	public ArrayList<Item> getItemsInCart() {
		return itemsInCart;
	}
	public float totalCost() {
        float total = 0;
        for (Item item : itemsInCart) { 
            total += item.getPrice() * item.getQuantity(); 
        }
        return total;
    }
	// Can kiem soat xem da co trong list hay chua
    public void addItem(Item item) {
	    if (item != null) {
	    	item.setQuantity(1);
	        itemsInCart.add(item);
	        System.out.println(item.getTitle() + " has been added to the Cart.");
	    } else {
	        System.out.println("Cannot add this Item to the Cart.");
	    }
	}
    public void changeQty(String id, int qty) {
    	Item item = searchOneById(id);
    	if (item == null) {
    		System.out.println("Item is not found in the Cart.");
    		return;
    	}
    	if (itemsInCart.contains(item)) {
    		if(qty == 0) {
    			itemsInCart.remove(item);
    			System.out.println(item.getTitle() + " has been removed from the Cart.");
    		}
    		else if (qty > 0) {
    			item.setQuantity(qty);
    			System.out.println("The quantity of " + item.getTitle() + "is updated to " + qty);
    		} else {
    			System.out.println("The quantity is invalid.");	
    		}
    	} 
    }
    public void removeItem(String id) {
    	Item item = searchOneById(id);
    	if(item == null) {
    		return;
    	}
	    if (itemsInCart.contains(item)) {
	    	itemsInCart.remove(item);
	        System.out.println(item.getTitle() + " has been removed from the Cart.");
	    } else {
	        System.out.println("Item is not found in the Cart.");
	    }
	}
	public ArrayList<Item> searchByTitle(String title) {
		searchItems.clear();
		for (Item item : itemsInCart) {
	        if (item.getTitle().contains(title)) {
	        	searchItems.add(item);
	        }
	    }
	    return searchItems;
	}
    public ArrayList<Item> searchById(String id) {
    	searchItems.clear();
		for (Item item : itemsInCart) {
	        if (item.getId().contains(id)) {
	        	searchItems.add(item);
	        }
	    }
	    return searchItems;
    }

    public ArrayList<Item> searchByType(String type) {
    	searchItems.clear();
		for (Item item : itemsInCart) {
	        if (item.getType().contains(type)) {
	        	searchItems.add(item);
	        }
	    }
	    return searchItems;
    }
    public Item searchOneById(String id) {
		for (Item item : itemsInCart) {
	        if (item.getId().equals(id)) {
	        	return item;
	        }
	    }
	    return null;
    }
    public void sortByTitleIncrease() {
        Collections.sort(itemsInCart, new ItemComparatorByTitleIncrease());
    }
    public void sortByTitleDecrease() {
        Collections.sort(itemsInCart, new ItemComparatorByTitleDecrease());
    }
    public void sortByTypeIncrease() {
        Collections.sort(itemsInCart, new ItemComparatorByTypeIncrease());
    }
    public void sortByTypeDecrease() {
        Collections.sort(itemsInCart, new ItemComparatorByTypeDecrease());
    }
    public void sortByPriceIncrease() {
        Collections.sort(itemsInCart, new ItemComparatorByPriceIncrease());
    }
    public void sortByPriceDecrease() {
        Collections.sort(itemsInCart, new ItemComparatorByPriceDecrease());
    }
    public void print() {
    	int i = 0;
        System.out.println("********************* Your Cart ***********************");
        System.out.println(String.format("%-4s %-10s %-20s %-10s %-15s %-15s %-10s %-10s %-8s",
    	        "Num", "ID", "Title", "Price", "Type", "Subtype", "Size", "Material", "Quantity"));
        for (Item item : itemsInCart) { 
        	System.out.println(String.format("%-3d. %-1s %-1d", ++i, item.toString(), item.getQuantity() ));  
        	}
        System.out.println("    Cost: " + totalCost());
        System.out.println("*******************************************************");
    }
}
