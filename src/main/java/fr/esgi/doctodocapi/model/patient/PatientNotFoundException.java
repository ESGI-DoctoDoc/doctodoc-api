package fr.esgi.doctodocapi.model.patient;

import fr.esgi.doctodocapi.model.DomainException;

public class PatientNotFoundException extends DomainException {
    private static final String CODE = "patient.not-found";
    private static final String MESSAGE = "Le patient n'existe pas.";

    public PatientNotFoundException() {
        super(CODE, MESSAGE);
    }
}
