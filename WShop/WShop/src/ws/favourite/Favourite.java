package ws.favourite;

import java.util.ArrayList;
import java.util.Collections;

import ws.item.Item;
import ws.item.ItemComparatorByPriceDecrease;
import ws.item.ItemComparatorByPriceIncrease;
import ws.item.ItemComparatorByTitleDecrease;
import ws.item.ItemComparatorByTitleIncrease;
import ws.item.ItemComparatorByTypeDecrease;
import ws.item.ItemComparatorByTypeIncrease;

public class Favourite {
	private ArrayList<Item> itemsInFavourite = new ArrayList<>();
	private ArrayList<Item> searchItems = new ArrayList<>();	
	public void addItem(Item item) {
		if (item != null) {
	        itemsInFavourite.add(item);
	        System.out.println(item.getTitle() + " has been added to the Favourite.");
	    } else {
	        System.out.println("Cannot add this Item to the Favourite.");
	    }
	}
	public void removeItem(String id) {
		Item item = searchOneById(id);
    	if(item == null) {
    		return;
    	}
	    if (itemsInFavourite.contains(item)) {
	        itemsInFavourite.remove(item);
	        System.out.println(item.getTitle() + " has been removed from the Favourite.");
	    } else {
	        System.out.println("Item is not found in the Favourite.");
	    }
	}
	public ArrayList<Item> searchByTitle(String title) {
		searchItems.clear();
		for (Item item : itemsInFavourite) {
	        if (item.getTitle().contains(title)) {
	        	searchItems.add(item);
	        }
	    }
	    return searchItems;
	}
    public ArrayList<Item> getItemsInFavourite() {
		return itemsInFavourite;
	}
	public ArrayList<Item> searchById(String id) {
    	searchItems.clear();
		for (Item item : itemsInFavourite) {
	        if (item.getId().contains(id)) {
	        	searchItems.add(item);
	        }
	    }
	    return searchItems;
    }
    public ArrayList<Item> searchByType(String type) {
    	searchItems.clear();
		for (Item item : itemsInFavourite) {
	        if (item.getType().contains(type)) {
	        	searchItems.add(item);
	        }
	    }
	    return searchItems;
    }
    public Item searchOneById(String id) {
		for (Item item : itemsInFavourite) {
	        if (item.getId().equals(id)) {
	        	return item;
	        }
	    }
	    return null;
    }
    public void sortByTitleIncrease() {
        Collections.sort(itemsInFavourite, new ItemComparatorByTitleIncrease());
    }
    public void sortByTitleDecrease() {
        Collections.sort(itemsInFavourite, new ItemComparatorByTitleDecrease());
    }
    public void sortByTypeIncrease() {
        Collections.sort(itemsInFavourite, new ItemComparatorByTypeIncrease());
    }
    public void sortByTypeDecrease() {
        Collections.sort(itemsInFavourite, new ItemComparatorByTypeDecrease());
    }
    public void sortByPriceIncrease() {
        Collections.sort(itemsInFavourite, new ItemComparatorByPriceIncrease());
    }
    public void sortByPriceDecrease() {
        Collections.sort(itemsInFavourite, new ItemComparatorByPriceDecrease());
    }
    public void print() {
    	int i = 0;
        System.out.println("******************* Your Favourite ********************");
        System.out.println(String.format("%-4s %-10s %-20s %-10s %-15s %-15s %-10s %-10s",
    	        "Num", "ID", "Title", "Price", "Type", "Subtype", "Size", "Material"));
        for (Item item : itemsInFavourite) {
        	System.out.println(String.format("%03d. %s", ++i, item.toString()));
        }
        System.out.println("*******************************************************");
    }
}

