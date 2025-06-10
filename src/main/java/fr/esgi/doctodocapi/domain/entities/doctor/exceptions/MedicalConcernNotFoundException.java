package fr.esgi.doctodocapi.domain.entities.doctor.exceptions;

import fr.esgi.doctodocapi.domain.DomainException;

public class MedicalConcernNotFoundException extends DomainException {
    private static final String CODE = "medical-concern.not-found";
    private static final String MESSAGE = "Le motif de consultation n'existe pas.";

    public MedicalConcernNotFoundException() {
        super(CODE, MESSAGE);
    }
}
