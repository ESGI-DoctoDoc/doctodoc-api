package fr.esgi.doctodocapi.model.admin.speciality;

import fr.esgi.doctodocapi.model.DomainException;

public class SpecialityAlreadyExistException extends DomainException {
    private static final String CODE = "speciality.already-exist";
    private static final String MESSAGE = "A speciality with this name already exists.";

    public SpecialityAlreadyExistException() {
        super(CODE, MESSAGE);
    }
}
