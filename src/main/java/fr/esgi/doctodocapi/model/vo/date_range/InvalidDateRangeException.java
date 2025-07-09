package fr.esgi.doctodocapi.model.vo.date_range;

import fr.esgi.doctodocapi.model.DomainException;

public class InvalidDateRangeException extends DomainException {

    private static final String CODE = "date-range.invalid";
    private static final String MESSAGE = "La date de début doit être avant ou égale à la date de fin.";

    public InvalidDateRangeException() {
        super(CODE, MESSAGE);
    }
}
