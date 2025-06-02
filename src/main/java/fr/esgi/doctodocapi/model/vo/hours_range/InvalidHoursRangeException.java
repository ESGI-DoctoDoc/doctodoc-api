package fr.esgi.doctodocapi.model.vo.hours_range;

import fr.esgi.doctodocapi.model.DomainException;

public class InvalidHoursRangeException extends DomainException {

    private static final String CODE = "invalidate-hours-exception.invalid";
    private static final String MESSAGE = "Le début de l'heure doit après la fin de l'heure.";


    public InvalidHoursRangeException() {
        super(CODE, MESSAGE);
    }
}
