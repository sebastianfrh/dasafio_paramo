package sat.recruitment.api.controllers;

import java.io.IOException;
import java.util.ArrayList;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.server.ResponseStatusException;

import sat.recruitment.api.core.contracts.User;
import sat.recruitment.api.core.errors.ExistingEntityException;
import sat.recruitment.api.core.errors.RepositoryException;
import sat.recruitment.api.core.errors.RestControllerError;
import sat.recruitment.api.core.usecases.CreateUserService;

@RestController
@RequestMapping(value = "/api/v1")
public class SatRecruitmentController {

	@Autowired
	private CreateUserService createUserService;

	@PostMapping(value = "/create-user", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createUser(@Valid @RequestBody User messageBody) {
		try {
			var result = this.createUserService.execute(messageBody);
		} catch (RepositoryException e) {
			throw new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY, e.getMessage());
		} catch (ExistingEntityException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		
	}

	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    public ArrayList<RestControllerError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ArrayList<RestControllerError> errors = new ArrayList<>();
        for(ObjectError error:ex.getBindingResult().getAllErrors()) {
        	errors.add(new RestControllerError(((FieldError) error).getField() +" "+ error.getDefaultMessage()));
        }
        return errors;
    }

}
