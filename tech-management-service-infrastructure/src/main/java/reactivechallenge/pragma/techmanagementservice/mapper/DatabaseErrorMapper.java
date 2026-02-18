package reactivechallenge.pragma.techmanagementservice.mapper;

import lombok.extern.slf4j.Slf4j;
import reactivechallenge.pragma.techmanagementservice.error.BusinessDomainException;
import reactivechallenge.pragma.techmanagementservice.exception.GenericDataBaseException;

@Slf4j
public class DatabaseErrorMapper {

    private DatabaseErrorMapper() {
    }

    public static Throwable map(Throwable e) {
        log.error("Error : {}", e.getLocalizedMessage());
        if (e instanceof org.springframework.dao.DataIntegrityViolationException) {

            if (e.getMessage().contains("technology.name_tecnology_unique")) {
                return new BusinessDomainException("Ya existe una tecnología con ese nombre registrado.");
            }

            return new GenericDataBaseException("Error de integridad: verifica los datos enviados.");
        }

        return new GenericDataBaseException("Error inesperado en la base de datos");
    }
}