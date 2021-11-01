package sat.recruitment.api.core.entities;

import org.apache.commons.csv.CSVRecord;

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
	
	public UserEntity(CSVRecord userRecord) {
		this.name = userRecord.get(0);
		this.email = userRecord.get(1);
		this.phone = userRecord.get(2);
		this.address = userRecord.get(3);
		this.userType = UserType.valueOf(userRecord.get(4).trim().toUpperCase());
		this.money = Double.valueOf(userRecord.get(5));
	}
	
	public boolean equals(Object o) {
		if (o == null || !(o instanceof UserEntity)) {
			return false;
		}

		UserEntity user = (UserEntity) o;

		if (user.getEmail().equals(this.getEmail()) || user.getPhone().equals(this.getPhone())) {
			return  true;
		} else if (user.getName().equals(this.getName()) && user.getAddress().equals(this.getAddress())) {
			return true;

		}

		return false;
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
