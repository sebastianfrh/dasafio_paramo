package sat.recruitment.api.repositories;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.tomcat.util.digester.DocumentProperties.Charset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import sat.recruitment.api.core.entities.UserEntity;
import sat.recruitment.api.core.errors.ExistingEntityException;
import sat.recruitment.api.core.errors.RepositoryException;
import sat.recruitment.api.core.providers.UserProvider;

@Service
public class UserRepository implements UserProvider {

	@Value("${spring.datasource.file.name}")
	private String fileName;

	@Autowired
	ResourceLoader resourceLoader;

	@Override
	public UserEntity save(UserEntity user) throws IOException {
		Resource resource = resourceLoader.getResource("classpath:" + fileName);
		// File file = resource.getFile();
		BufferedWriter writer = Files.newBufferedWriter(Paths.get(resource.getURI()), StandardCharsets.UTF_8,
				StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
		CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
		printer.printRecord(user.toArrayOfStrings());
		writer.close();
		printer.close();

		return user;
	}

	@Override
	public boolean exists(UserEntity user) throws RepositoryException, IOException {
		Resource resource = resourceLoader.getResource("classpath:" + fileName);
		File file;
		Reader reader = null;
		Iterable<CSVRecord> records;

		try {
			file = resource.getFile();
			if (!file.exists()) {
				file.createNewFile();
			}
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
