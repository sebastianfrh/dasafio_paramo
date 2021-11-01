package sat.recruitment.api.core.errors;

public class ExistingEntityException extends Exception{
	
	public ExistingEntityException(String errorMessage) {
        super(errorMessage);
    }

}
