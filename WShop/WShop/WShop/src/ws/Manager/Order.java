package ws.Manager;

public class Order {
	protected int id;
	protected String name; //thra la email cua ng dat
	protected String attitude; //status nhung Linh ko cho sua
	protected String address;
	protected String pnum;
	protected String orderTime;
	protected float total;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAttitude() {
		return attitude;
	}
	public void setAttitude(String attitude) {
		this.attitude = attitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPnum() {
		return pnum;
	}
	public void setPnum(String pnum) {
		this.pnum = pnum;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public float getTotal() {
		return total;
	}
	public void setTotal(float total) {
		this.total = total;
	}
	
	public Order(int id, String name, String attitude, String address, String pnum, String orderTime, float total) {
		super();
		this.id = id;
		this.name = name;
		this.attitude = attitude;
		this.address = address;
		this.pnum = pnum;
		this.orderTime = orderTime;
		this.total = total;
	}
	public String toString() {
		return String.format("%-15s %-12s %-50s %-10s",
		        getName(),        
		        getPnum(),     
		        getAddress(),
		        getAttitude()     
		    );
	}
}
