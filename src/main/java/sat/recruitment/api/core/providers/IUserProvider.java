package sat.recruitment.api.core.providers;

import java.io.IOException;

import sat.recruitment.api.core.entities.UserEntity;
import sat.recruitment.api.core.errors.ExistingEntityException;
import sat.recruitment.api.core.errors.RepositoryException;

public interface IUserProvider {
	public UserEntity save(UserEntity user) throws RepositoryException, IOException, ExistingEntityException;
	public boolean exists(UserEntity user) throws RepositoryException, IOException;
}
