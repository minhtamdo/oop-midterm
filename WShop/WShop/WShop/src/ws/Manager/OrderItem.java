package ws.Manager;

public class OrderItem {
    private String productId;
    private String title;  // Thêm title vào lớp OrderItem
    private int orderId;
    private int quantity;
    private float price;

    public OrderItem(String productId, String title, int orderId, int quantity, float price) {
        this.productId = productId;
        this.title = title;  // Gán giá trị title
        this.orderId = orderId;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public String getTitle() {
        return title;  // Getter cho title
    }

    public int getOrderId() {
        return orderId;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getPrice() {
        return price;
    }
}
