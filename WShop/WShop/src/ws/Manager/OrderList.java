package ws.Manager;

import java.util.ArrayList;

public class OrderList {
	ArrayList<Order> orderList = new ArrayList<>();
	
	public void printOrderList() {
		System.out.println("********************* Order List *********************");
		for(Order order : orderList) {
			System.out.println(order.toString());
		}
	}
	
	public void addOrder(int id, String name, String pnum, String address) {
		orderList.add(new Order(id, name, pnum, address));
	}
	
	public OrderList() {}
		
	public ArrayList<Order> getOrderList() {
		return orderList;
	}

	public void setOrderList(ArrayList<Order> orderList) {
		this.orderList = orderList;
	}
	
}
