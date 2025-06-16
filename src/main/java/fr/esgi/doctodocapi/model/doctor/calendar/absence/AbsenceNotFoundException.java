package fr.esgi.doctodocapi.model.doctor.calendar.absence;

import fr.esgi.doctodocapi.model.DomainException;

public class AbsenceNotFoundException extends DomainException {
    private static final String CODE = "absence.not-found";
    private static final String MESSAGE = "L'absence n'existe pas.";

    public AbsenceNotFoundException() {
        super(CODE, MESSAGE);
    }
}
