package sat.recruitment.api.core.usecases;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sat.recruitment.api.core.contracts.User;
import sat.recruitment.api.core.entities.UserEntity;
import sat.recruitment.api.core.errors.ExistingEntityException;
import sat.recruitment.api.core.errors.RepositoryException;
import sat.recruitment.api.core.providers.IUserProvider;

@Service
public class CreateUser implements ICreateUser{
	
	@Autowired
	private IUserProvider userProvider;
	private List<User> users = new ArrayList<User>();
	
	@Override
	public UserEntity execute(User userRequest) throws RepositoryException, IOException, ExistingEntityException{
		var newUser = new UserEntity(userRequest);
		var gif = Double.valueOf(newUser.getMoney()) * calculateGifPercentage(newUser);
		newUser.setMoney(newUser.getMoney() + gif);
		
		return userProvider.save(newUser);

		/*InputStream fstream;
		try {
			fstream = getClass().getResourceAsStream("/users.txt");

			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;

			while ((strLine = br.readLine()) != null) {
				String[] line = strLine.split(",");
				User user = new User();
				user.setName(line[0]);
				user.setEmail(line[1]);
				user.setPhone(line[2]);
				user.setAddress(line[3]);
				//user.setUserType(line[4]);
				user.setMoney(Double.valueOf(line[5]));
				users.add(user);

			}
			fstream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Boolean isDuplicated = false;
		for (User user : users) {

			if (user.getEmail().equals(newUser.getEmail()) || user.getPhone().equals(newUser.getPhone())) {
				isDuplicated = true;
			} else if (user.getName().equals(newUser.getName())) {
				if (user.getAddress().equals(newUser.getAddress())) {
					isDuplicated = true;
				}

			}
		}
		if (isDuplicated) {
			//throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is duplicated");
			return false;
		}*/
	}
	
	private Double calculateGifPercentage(UserEntity user) {
		var percentage = Double.valueOf(0);
		var money = Double.valueOf(user.getMoney());
		
		switch(user.getUserType()) {
		case NORMAL:
			if (money >= 100) {
				percentage = Double.valueOf(0.12);
			} else if (money > 10) {
				percentage = Double.valueOf(0.8);
			}
			break;
		case SUPERUSER:
			if (money > 100) {
				percentage = Double.valueOf(0.2);
			}
			break;
		case PREMIUM:
			if (money > 100) {
				percentage = Double.valueOf(2);
			}
			break;
		default:
			break;
		}

		return percentage;
	}

}
