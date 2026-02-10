package reactivechallenge.pragma.techmanagementservice.model;

import reactivechallenge.pragma.techmanagementservice.error.BusinessDomainException;

public record TechnologyModel(Long id, String name, String description) implements IStringVerifier{
    public TechnologyModel{
        validateField(name, "name");
        validateField(description, "description");
        validateFieldLength(name, "name", 50);
        validateFieldLength(description, "description", 90);
    }

    private void validateField(String value, String fieldName) {
        if(verify(value).isEmpty()){
            throw  new BusinessDomainException(String.format("El campo %s no puede estar vacío", fieldName));
        }
    }

    private void validateFieldLength(String value, String fieldName, int maxLength) {
        if(verify(value).length() > maxLength){
            throw  new BusinessDomainException(String.format("El campo %s no puede tener más de %d caracteres", fieldName, maxLength));
        }
    }
}