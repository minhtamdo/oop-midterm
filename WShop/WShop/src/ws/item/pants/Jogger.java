package ws.item.pants;

import ws.item.Item;

public class Jogger extends Item {
	private int size;
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public Jogger(String id, String title, float price, String type, String material, int size) {
		super(id, title, price, type, material);
		this.size = size;
		}
	@Override
	public String toString() {
	    return String.format(
	        "%-10s %-20s %-10.2f %-15s %-15s %-10s %-10s",
	        getId(),          
	        getTitle(),       
	        getPrice(),       
	        getType(),        
	        "",     
	        getSize(),
	        getMaterial()     
	    );
	}
}
