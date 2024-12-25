package ws.item.accessories;

import ws.item.Item;

public class Hat extends Item {
	private String subtype; // cap, snapback, bucket, beanie
	public String getSubtype() {
		return subtype;
	}
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
	public Hat(String id, String title, float price, String type, String material, String subtype) {
		super(id, title, price, type, material);
		this.subtype = subtype;
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
	        "Free size",
	        getMaterial()     
	    );
	}
}