package sat.recruitment.api.core.contracts;

import javax.validation.constraints.NotNull;

import sat.recruitment.api.core.entities.UserType;

public class User {
	
	@NotNull (message = "is required")
	private String name;
	
	@NotNull (message = "is required")
	private String email;
	
	@NotNull (message = "is required")
	private String address;
	
	@NotNull (message = "is required")
	private String phone;
	
	private UserType userType;
	
	private Double money;
	
	public User() {
		
	}

	public User(String name, String email, String address, String phone, UserType userType, Double money) {
		super();
		this.name = name;
		this.email = email;
		this.address = address;
		this.phone = phone;
		this.userType = userType;
		this.money = money;
	}

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}
}