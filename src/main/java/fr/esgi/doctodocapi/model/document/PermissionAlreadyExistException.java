package fr.esgi.doctodocapi.model.document;

import fr.esgi.doctodocapi.model.DomainException;

public class PermissionAlreadyExistException extends DomainException {
    private static final String CODE = "permission.already-exist";
    private static final String MESSAGE = "Une permission existe déjà";

    public PermissionAlreadyExistException() {
        super(CODE, MESSAGE);
    }
}
