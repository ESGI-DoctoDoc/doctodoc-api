package fr.esgi.doctodocapi.model.vo.day_of_month;

import fr.esgi.doctodocapi.model.DomainException;

public class InvalidDayOfMonthException extends DomainException {

    private static final String CODE = "day-number.invalid";
    private static final String MESSAGE = "Le numéro du jour du mois doit être entre 1 et 31.";

    public InvalidDayOfMonthException() {
        super(CODE, MESSAGE);
    }
}
