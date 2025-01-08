package ws.person;
public class Person {
    private int id;
    private String username; 
    private String password;
    private String name;
    private String email;
    private String phone;
    private String role;

    

	public Person(int id, String username, String password, String name, String email, String phone, String role) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.role = role;
	}

	public String getEmail() {
		return email;
	}
	
	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public String getRole() {
		return role;
	}

}
