package sat.recruitment.api.core.usecases;

import java.io.IOException;

import sat.recruitment.api.core.contracts.User;
import sat.recruitment.api.core.entities.UserEntity;
import sat.recruitment.api.core.errors.ExistingEntityException;
import sat.recruitment.api.core.errors.RepositoryException;

public interface ICreateUser {
	public UserEntity execute(User userRequest) throws RepositoryException, IOException, ExistingEntityException;
}
