package fr.esgi.doctodocapi.model.admin.speciality;

import fr.esgi.doctodocapi.model.DomainException;

public class SpecialityNotFoundException extends DomainException {
    private static final String CODE = "speciality.not-found";
    private static final String MESSAGE = "La specialité n'existe pas.";
    public SpecialityNotFoundException() {
        super(CODE, MESSAGE);
    }
}
