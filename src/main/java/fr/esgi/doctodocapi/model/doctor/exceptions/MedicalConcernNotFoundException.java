package fr.esgi.doctodocapi.model.doctor.exceptions;

import fr.esgi.doctodocapi.model.DomainException;

public class MedicalConcernNotFoundException extends DomainException {
    private static final String CODE = "medical-concern.not-found";
    private static final String MESSAGE = "Le motif de consultation n'existe pas.";

    public MedicalConcernNotFoundException() {
        super(CODE, MESSAGE);
    }
}
