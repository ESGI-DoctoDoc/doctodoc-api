package fr.esgi.doctodocapi.model.doctor.calendar.absence;

import fr.esgi.doctodocapi.model.DomainException;

public class ExpiredAbsenceException extends DomainException {
    private static final String CODE = "absence.exprired";
    private static final String MESSAGE = "L'absence est passé.";

    public ExpiredAbsenceException() {
        super(CODE, MESSAGE);
    }
}