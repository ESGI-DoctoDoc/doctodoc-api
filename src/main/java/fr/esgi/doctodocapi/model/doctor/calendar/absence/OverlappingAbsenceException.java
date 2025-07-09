package fr.esgi.doctodocapi.model.doctor.calendar.absence;

import fr.esgi.doctodocapi.model.DomainException;

public class OverlappingAbsenceException extends DomainException {
    private static final String CODE = "absence.conflict";
    private static final String MESSAGE = "Une absence est déjà enregistrée à cette date ou sur ce créneau.";

    public OverlappingAbsenceException() {
        super(CODE, MESSAGE);
    }
}