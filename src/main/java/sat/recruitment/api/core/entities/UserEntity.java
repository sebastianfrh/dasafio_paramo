package sat.recruitment.api.core.entities;

import sat.recruitment.api.core.contracts.User;

public class UserEntity {
	
	private String name;
	private String email;
	private String address;
	private String phone;
	private UserType userType;
	private Double money;
	
	
	public UserEntity(User userRequest) {
		this.name = userRequest.getName();
		this.email = userRequest.getEmail();
		this.address = userRequest.getAddress();
		this.phone = userRequest.getPhone();
		this.userType = userRequest.getUserType();
		this.money = userRequest.getMoney();
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
