package ws.store;

import java.util.ArrayList;
import java.util.Collections;

import ws.item.Item;
import ws.item.ItemComparatorByPriceDecrease;
import ws.item.ItemComparatorByPriceIncrease;
import ws.item.ItemComparatorByTitleDecrease;
import ws.item.ItemComparatorByTitleIncrease;
import ws.item.ItemComparatorByTypeDecrease;
import ws.item.ItemComparatorByTypeIncrease;

public class Store {
	private ArrayList<Item> itemsInStore = new ArrayList<>();
	private ArrayList<Item> searchItems = new ArrayList<>();	
	public void addItem(Item item) {
	    if (item != null) {
	        itemsInStore.add(item);
	        System.out.println(item.getTitle() + " has been added to the store.");
	    } else {
	        System.out.println("Cannot add this Item to the store.");
	    }
	}
	public void removeItem(String id) {
		Item item = searchOneById(id);
    	if (item == null) {
    		System.out.println("Item is not found in the Store.");
    		return;
    	}
	    if (itemsInStore.contains(item)) {
	        itemsInStore.remove(item);
	        System.out.println(item.getTitle() + " has been removed from the store.");
	    } else {
	        System.out.println("Item is not found in the store.");
	    }
	}
	public ArrayList<Item> getItemsInStore() {
		return itemsInStore;
	}
	public ArrayList<Item> searchByTitle(String title) {
		searchItems.clear();
		for (Item item : itemsInStore) {
	        if (item.getTitle().contains(title)) {
	        	searchItems.add(item);
	        }
	    }
	    return searchItems;
	}
    public ArrayList<Item> searchById(String id) {
    	searchItems.clear();
		for (Item item : itemsInStore) {
	        if (item.getId().contains(id)) {
	        	searchItems.add(item);
	        }
	    }
	    return searchItems;
    }
    public ArrayList<Item> searchByType(String type) {
    	searchItems.clear();
		for (Item item : itemsInStore) {
	        if (item.getType().contains(type)) {
	        	searchItems.add(item);
	        }
	    }
	    return searchItems;
    }
    public Item searchOneById(String id) {
		for (Item item : itemsInStore) {
	        if (item.getId().equals(id)) {
	        	return item;
	        }
	    }
	    return null;
    }
    public void sortByTitleIncrease() {
        Collections.sort(itemsInStore, new ItemComparatorByTitleIncrease());
    }
    public void sortByTitleDecrease() {
        Collections.sort(itemsInStore, new ItemComparatorByTitleDecrease());
    }
    public void sortByTypeIncrease() {
        Collections.sort(itemsInStore, new ItemComparatorByTypeIncrease());
    }
    public void sortByTypeDecrease() {
        Collections.sort(itemsInStore, new ItemComparatorByTypeDecrease());
    }
    public void sortByPriceIncrease() {
        Collections.sort(itemsInStore, new ItemComparatorByPriceIncrease());
    }
    public void sortByPriceDecrease() {
        Collections.sort(itemsInStore, new ItemComparatorByPriceDecrease());
    }
	
    public void print() {
    	int i = 0;
        System.out.println("********************** 9S Store ***********************");
        System.out.println(String.format("%-4s %-10s %-20s %-10s %-15s %-15s %-10s %-10s",
    	        "Num", "ID", "Title", "Price", "Type", "Subtype", "Size", "Material"));
        for (Item item : itemsInStore) {
        	System.out.println(String.format("%03d. %s", ++i, item.toString()));
        }
        System.out.println("*******************************************************");
    }
}

