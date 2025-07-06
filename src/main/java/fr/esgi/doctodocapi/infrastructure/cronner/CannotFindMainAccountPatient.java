package fr.esgi.doctodocapi.infrastructure.cronner;

import java.util.UUID;

public class CannotFindMainAccountPatient extends RuntimeException {
    public CannotFindMainAccountPatient(UUID id) {
        super("Cannot find main account patient with id :" + id);
    }
}
