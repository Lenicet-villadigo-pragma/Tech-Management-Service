package reactivechallenge.pragma.techmanagementservice.mapper;

import reactivechallenge.pragma.techmanagementservice.error.RegisterAlreadyExistsException;
import reactivechallenge.pragma.techmanagementservice.error.GenericDataBaseException;

public class DatabaseErrorMapper {

    private DatabaseErrorMapper() {
    }

    public static Throwable map(Throwable e) {
        if (e instanceof org.springframework.dao.DataIntegrityViolationException) {

            if (e.getMessage().contains("technology.name_tecnology_unique")) {
                return new RegisterAlreadyExistsException("Ya existe una tecnología con ese nombre registrado.");
            }

            return new GenericDataBaseException("Error de integridad: verifica los datos enviados.");
        }

        return new GenericDataBaseException("Error inesperado en la base de datos");
    }
}