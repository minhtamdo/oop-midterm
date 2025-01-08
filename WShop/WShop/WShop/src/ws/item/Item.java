package ws.item;

import java.util.Comparator;

public class Item extends AbstractItem {
    protected String type;
    protected String material;
    protected int quantity;

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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public static final Comparator<Item> COMPARE_BY_TITLE_INCREASE = new ItemComparatorByTitleIncrease();
    public static final Comparator<Item> COMPARE_BY_TITLE_DECREASE = new ItemComparatorByTitleDecrease();
    public static final Comparator<Item> COMPARE_BY_PRICE_INCREASE = new ItemComparatorByPriceIncrease();
    public static final Comparator<Item> COMPARE_BY_PRICE_DECREASE = new ItemComparatorByPriceDecrease();
    public static final Comparator<Item> COMPARE_BY_TYPE_INCREASE = new ItemComparatorByTypeIncrease();
    public static final Comparator<Item> COMPARE_BY_TYPE_DECREASE = new ItemComparatorByTypeDecrease();

    public Item(String id, float price, int quantity) {
        super();
        this.id = id;
        this.price = price;
        this.quantity = quantity;
    }

    public Item(String id, String title, float price, String type, String material) {
        super();
        this.id = id;
        this.title = title;
        this.price = price;
        this.type = type;
        this.material = material;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Item { ID: " + id + ", Title: " + title + ", Price: " + price + "$ }";
    }
}
