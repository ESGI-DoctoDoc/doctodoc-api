package fr.esgi.doctodocapi.domain.entities.doctor.exceptions;

import fr.esgi.doctodocapi.domain.DomainException;

public class DoctorNotFoundException extends DomainException {
    private static final String CODE = "doctor.not-found";
    private static final String MESSAGE = "Le docteur n'existe pas.";

    public DoctorNotFoundException() {
        super(CODE, MESSAGE);
    }
}
