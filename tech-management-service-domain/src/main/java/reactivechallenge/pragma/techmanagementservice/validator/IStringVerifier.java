package reactivechallenge.pragma.techmanagementservice.validator;

public interface IStringVerifier {
    default String verify(String value) {
       return value==null? "":value.toLowerCase().replaceAll("\\s+", " ").trim();
    }
}
