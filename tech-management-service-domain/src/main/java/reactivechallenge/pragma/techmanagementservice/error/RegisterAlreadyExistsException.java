package reactivechallenge.pragma.techmanagementservice.error;

public class RegisterAlreadyExistsException extends RuntimeException {
    public RegisterAlreadyExistsException() {
        super();
    }

    public RegisterAlreadyExistsException(String message) {
        super(message);
    }
}
