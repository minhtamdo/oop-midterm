package ws.item.outerwear;

import ws.item.Item;

public class Coat extends Item {
	private int size;
	private String subtype; // mangto, bomber, windbreaker, puffer jacket
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getSubtype() {
		return subtype;
	}
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
	public Coat(String id, String title, float price, String type, String material, String subtype, int size) {
		super(id, title, price, type, material);
		this.subtype = subtype;
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
	        getSubtype(),     
	        getSize(),
	        getMaterial()     
	    );
	}
}
/*
 
 */







