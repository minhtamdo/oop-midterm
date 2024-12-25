package a;

public class MainTest {
    public static void main(String[] args) {
        ProductDAO dao = new ProductDAO();

        // Thêm sản phẩm
        dao.addProduct("T-Shirt", 19.99, 50);
        dao.addProduct("Jeans", 49.99, 30);

        // Hiển thị sản phẩm
        System.out.println("Danh sách sản phẩm:");
        dao.getAllProducts();
    }
}

