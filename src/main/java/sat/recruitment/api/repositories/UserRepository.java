package sat.recruitment.api.repositories;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import sat.recruitment.api.core.entities.UserEntity;
import sat.recruitment.api.core.errors.ExistingEntityException;
import sat.recruitment.api.core.errors.RepositoryException;
import sat.recruitment.api.core.providers.IUserProvider;

@Service
public class UserRepository implements IUserProvider{
	
	@Value("${spring.datasource.file.name}")
	private String fileName;
	
	@Autowired
	ResourceLoader resourceLoader;

	@Override
	public UserEntity save(UserEntity user) throws RepositoryException, IOException, ExistingEntityException{
		if(!exists(user)) {
			//guardarlo
		} else {
			throw new ExistingEntityException(user.getName() + " already exists");
		}
		return null;
	}

	@Override
	public boolean exists(UserEntity user) throws RepositoryException, IOException{
		Resource resource=resourceLoader.getResource("classpath:"+fileName);
		File file;
		Reader reader = null;
		Iterable<CSVRecord> records;
		
		try {
			file = resource.getFile();
			reader = new FileReader(file);
			records = CSVFormat.RFC4180.parse(reader);
			
			for (CSVRecord record : records) {
			    var userFromRecord = new UserEntity(record);
			    if (user.equals(userFromRecord)) {
			    	return true;
			    }
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RepositoryException(e.getMessage());
		} finally {
			reader.close();
		}
		
		return false;
	}

}
