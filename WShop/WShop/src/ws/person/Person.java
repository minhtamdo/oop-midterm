package ws.person;

public class Person {
	private String tk;
	private String mk;
	private String name;
	private String email;
	private String role; // Manager, Guest
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	
	public String getTk() {
		return tk;
	}

	public void setTk(String tk) {
		this.tk = tk;
	}

	public String getMk() {
		return mk;
	}

	public void setMk(String mk) {
		this.mk = mk;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Person(String tk, String mk, String name, String email, String role) {
		super();
		this.tk = tk;
		this.mk = mk;
		this.name = name;
		this.email = email;
		this.role = role;
	}

}
