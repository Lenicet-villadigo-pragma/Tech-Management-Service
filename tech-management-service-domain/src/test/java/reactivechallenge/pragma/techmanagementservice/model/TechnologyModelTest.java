package reactivechallenge.pragma.techmanagementservice.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactivechallenge.pragma.techmanagementservice.error.BusinessDomainException;

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

    @Test
    void buildTechModelThrowError_emptyName(){
        // Arrange
        TechnologyModel technologyModel = null;
        BusinessDomainException businessDomainException = null;

        // Act
        try {
            technologyModel = new TechnologyModel(null, "", "description");
        } catch (Exception e) {
            businessDomainException = new BusinessDomainException(e.getMessage());
        }

        //Assert
        Assertions.assertNotNull(businessDomainException);
        Assertions.assertNull(technologyModel);
        Assertions.assertEquals("El campo name no puede estar vacío", businessDomainException.getMessage());
    }

    @Test
    void buildTechModelThrowError_emptyDescription(){
        // Arrange
        TechnologyModel technologyModel = null;
        BusinessDomainException businessDomainException = null;

        // Act
        try {
            technologyModel = new TechnologyModel(null, "name", "");
        } catch (Exception e) {
            businessDomainException = new BusinessDomainException(e.getMessage());
        }

        //Assert
        Assertions.assertNotNull(businessDomainException);
        Assertions.assertNull(technologyModel);
        Assertions.assertEquals("El campo description no puede estar vacío", businessDomainException.getMessage());
    }

    @Test
    void buildTechModelThrowError_tooLongName(){
        // Arrange
        TechnologyModel technologyModel = null;
        BusinessDomainException businessDomainException = null;

        // Act
        try {
            technologyModel = new TechnologyModel(null, "0123456789 0123456789 0123456789 0123456789 0123456789 0123456789"
                    , "description");
        } catch (Exception e) {
            businessDomainException = new BusinessDomainException(e.getMessage());
        }

        //Assert
        Assertions.assertNotNull(businessDomainException);
        Assertions.assertNull(technologyModel);
        Assertions.assertEquals("El campo name no puede tener más de 50 caracteres", businessDomainException.getMessage());
    }

    @Test
    void buildTechModelThrowError_tooLongDescription(){
        // Arrange
        TechnologyModel technologyModel = null;
        BusinessDomainException businessDomainException = null;

        // Act
        try {
            technologyModel = new TechnologyModel(null, "name"
                    , "0123456789 0123456789 0123456789 0123456789 0123456789 0123456789 0123456789 0123456789 0123456789 0123456789");
        } catch (Exception e) {
            businessDomainException = new BusinessDomainException(e.getMessage());
        }

        //Assert
        Assertions.assertNotNull(businessDomainException);
        Assertions.assertNull(technologyModel);
        Assertions.assertEquals("El campo description no puede tener más de 90 caracteres", businessDomainException.getMessage());
    }

    
}
