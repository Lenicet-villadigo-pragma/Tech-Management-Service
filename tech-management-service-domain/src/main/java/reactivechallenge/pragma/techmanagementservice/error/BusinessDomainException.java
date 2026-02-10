package reactivechallenge.pragma.techmanagementservice.error;

public class BusinessDomainException extends RuntimeException {
    public BusinessDomainException() {
        super();
    }

    public BusinessDomainException(String message) {
        super(message);
    }
}
