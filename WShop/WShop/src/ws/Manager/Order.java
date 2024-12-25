package ws.Manager;

public class Order {
	protected int id;
	protected String name; //thra la email cua ng dat
	protected String pnum;
	protected String address;
	protected String attitude; //status nhung Linh ko cho sua
	public String getName() {
		return name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPnum() {
		return pnum;
	}
	public void setPnum(String pnum) {
		this.pnum = pnum;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAttitude() {
		return attitude;
	}
	public void setAttitude(String attitude) {
		this.attitude = attitude;
	}
	public Order(int id, String name, String pnum, String address) {
		super();
		this.id = id;
		this.name = name;
		this.pnum = pnum;
		this.address = address;
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
