package reactivechallenge.pragma.techmanagementservice.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import reactivechallenge.pragma.techmanagementservice.error.BusinessDomainException;

import java.util.stream.Stream;

class TechnologyModelTest {

    @Test
    void buildTechModelSuccessfully(){
        // Arrange
        TechnologyModel technologyModel = null;
        BusinessDomainException businessDomainException = null;

        // Act
        try {
            technologyModel = new TechnologyModel(null, "name", "description");
        } catch (Exception e) {
            businessDomainException = new BusinessDomainException(e.getMessage());
        }

        //Assert
        Assertions.assertNotNull(technologyModel);
        Assertions.assertNull(businessDomainException);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTechnologyData")
    void buildTechModelThrowError(String name, String description, String expectedMessage) {
        // Arrange
        TechnologyModel technologyModel = null;
        BusinessDomainException businessDomainException = null;

        // Act
        try {
            technologyModel = new TechnologyModel(null, name, description);
        } catch (Exception e) {
            businessDomainException = new BusinessDomainException(e.getMessage());
        }

        //Assert
        Assertions.assertNotNull(businessDomainException);
        Assertions.assertNull(technologyModel);
        Assertions.assertEquals(expectedMessage, businessDomainException.getMessage());
    }

    private static Stream<Arguments> provideInvalidTechnologyData() {
        return Stream.of(
                Arguments.of("", "description", "El campo name no puede estar vacío"),
                Arguments.of("name", "", "El campo description no puede estar vacío"),
                Arguments.of("0123456789 0123456789 0123456789 0123456789 0123456789 0123456789",
                        "description", "El campo name no puede tener más de 50 caracteres"),
                Arguments.of("name",
                        "0123456789 0123456789 0123456789 0123456789 0123456789 0123456789 0123456789 0123456789 0123456789 0123456789",
                        "El campo description no puede tener más de 90 caracteres")
        );
    }
}

