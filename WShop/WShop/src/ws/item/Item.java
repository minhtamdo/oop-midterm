package ws.item;

import java.util.Comparator;
	
public class Item {
	protected String id;
	protected String title;
	protected float price;
	protected String type;
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	protected String material;
	protected int quantity;

	public static final Comparator<Item> COMPARE_BY_TITLE_INCREASE = new ItemComparatorByTitleIncrease();
	public static final Comparator<Item> COMPARE_BY_TITLE_DECREASE = new ItemComparatorByTitleDecrease();
	public static final Comparator<Item> COMPARE_BY_PRICE_INCREASE = new ItemComparatorByPriceIncrease();
	public static final Comparator<Item> COMPARE_BY_PRICE_DECREASE = new ItemComparatorByPriceDecrease();
	public static final Comparator<Item> COMPARE_BY_TYPE_INCREASE = new ItemComparatorByTypeIncrease();
	public static final Comparator<Item> COMPARE_BY_TYPE_DECREASE = new ItemComparatorByTypeDecrease();

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public Item(String id, String title, float price, String type, String material) {
		super();
		this.id = id;
		this.title = title;
		this.price = price;
		this.type = type;
		this.material = material;
	}
	public String toString() {
	    return "Item { " + getId() + ". " + getTitle() + " - " + getPrice() + "$ - " + getType() + " - " + getMaterial() + "}";
	}
	
}
