package fr.esgi.doctodocapi.model.doctor.calendar.slot;

import fr.esgi.doctodocapi.model.DomainException;

public class RecurrenceTypeNotFound extends DomainException {
    private static final String CODE = "recurrence-type.not-found";
    private static final String MESSAGE = "Le type de recurrence n'existe pas.";

    public RecurrenceTypeNotFound() {
        super(CODE, MESSAGE);
    }
}