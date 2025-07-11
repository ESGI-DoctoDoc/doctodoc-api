package fr.esgi.doctodocapi.model.doctor.calendar.slot;

import fr.esgi.doctodocapi.model.DomainException;

public class AtLeastOneMedicalConcernException extends DomainException {
    private static final String CODE = "medical-concern.empty";
    private static final String MESSAGE = "Au moins un motif médical doit être associé.";

    public AtLeastOneMedicalConcernException() {
        super(CODE, MESSAGE);
    }
}