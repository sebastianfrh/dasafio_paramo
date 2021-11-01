package sat.recruitment.api.core.usecases;

import sat.recruitment.api.core.contracts.User;

public interface ICreateUser {
	public boolean execute(User userRequest);
}
