package reactivechallenge.pragma.techmanagementservice.model;

public interface IStringVerifier {
    default String verify(String value) {
       return value==null? "":value.toLowerCase().replaceAll("\\s+", " ").trim();
    }
}
