package sat.recruitment.api.core.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sat.recruitment.api.core.contracts.User;
import sat.recruitment.api.core.entities.UserEntity;
import sat.recruitment.api.core.errors.ExistingEntityException;
import sat.recruitment.api.core.errors.RepositoryException;
import sat.recruitment.api.core.providers.UserProvider;

@Service
public class CreateUserService {

	@Autowired
	private UserProvider userProvider;

	public CreateUserService(UserProvider userProvider) {
		this.userProvider = userProvider;
	}

	public UserEntity execute(User userRequest) throws RepositoryException, IOException, ExistingEntityException {
		var newUser = new UserEntity(userRequest);
		var gif = Double.valueOf(newUser.getMoney()) * calculateGifPercentage(newUser);
		newUser.setMoney(newUser.getMoney() + gif);

		if (!userProvider.exists(newUser)) {
			return userProvider.save(newUser);
		} else {
			throw new ExistingEntityException("user "+ newUser.getName() + " already exists");
		}
	}

	private Double calculateGifPercentage(UserEntity user) {
		var percentage = Double.valueOf(0);
		var money = Double.valueOf(user.getMoney());

		switch (user.getUserType()) {
		case NORMAL:
			if (money >= 100) {
				percentage = Double.valueOf(0.12);
			} else if (money > 10) {
				percentage = Double.valueOf(0.08);
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
