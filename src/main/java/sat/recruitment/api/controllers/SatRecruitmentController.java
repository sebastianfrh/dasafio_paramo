package sat.recruitment.api.controllers;

import java.io.IOException;
import java.util.ArrayList;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sat.recruitment.api.core.contracts.UserRequest;
import sat.recruitment.api.core.errors.ExistingEntityException;
import sat.recruitment.api.core.errors.RepositoryException;
import sat.recruitment.api.core.errors.RestControllerError;
import sat.recruitment.api.core.usecases.CreateUserUseCase;

@RestController
@RequestMapping(value = "/api/v1")
public class SatRecruitmentController {

	private final CreateUserUseCase createUserService;
	private static final Logger logger = LogManager.getLogger(SatRecruitmentController.class);

	public SatRecruitmentController(CreateUserUseCase createUserService) {
		this.createUserService = createUserService;
	}

	@PostMapping(value = "/create-user", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createUser(@Valid @RequestBody UserRequest messageBody) throws Exception {
		try {
			this.createUserService.execute(messageBody);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ArrayList<RestControllerError> handleValidationExceptions(MethodArgumentNotValidException ex) {
		ArrayList<RestControllerError> errors = new ArrayList<>();
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			errors.add(new RestControllerError(((FieldError) error).getField() + " " + error.getDefaultMessage()));
		}
		return errors;
	}

	@ExceptionHandler(ExistingEntityException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public RestControllerError handleExistingEntityException(ExistingEntityException ex) {
		return new RestControllerError(ex.getMessage());
	}

	@ExceptionHandler(RepositoryException.class)
	@ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
	public RestControllerError handleRepositoryException(RepositoryException ex) {
		return new RestControllerError(ex.getMessage());
	}

	@ExceptionHandler(IOException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public RestControllerError handleIOException(IOException ex) {
		return new RestControllerError(ex.getMessage());
	}

}
