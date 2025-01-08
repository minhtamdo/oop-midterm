package ws.item;

public abstract class AbstractItem {
    protected String id;
    protected String title;
    protected float price;

    public abstract String getId();
    public abstract void setId(String id);
    public abstract String getTitle();
    public abstract void setTitle(String title);
    public abstract float getPrice();
    public abstract void setPrice(float price);

    @Override
    public String toString() {
        return "AbstractItem { ID: " + id + ", Title: " + title + ", Price: " + price + " }";
    }
}
