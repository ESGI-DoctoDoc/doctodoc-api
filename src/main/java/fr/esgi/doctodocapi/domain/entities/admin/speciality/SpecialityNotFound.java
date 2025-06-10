package fr.esgi.doctodocapi.domain.entities.admin.speciality;

import fr.esgi.doctodocapi.domain.DomainException;

public class SpecialityNotFound extends DomainException {
    private static final String CODE = "speciality.not-found";
    private static final String MESSAGE = "La specialité n'existe pas.";
    public SpecialityNotFound() {
        super(CODE, MESSAGE);
    }
}
