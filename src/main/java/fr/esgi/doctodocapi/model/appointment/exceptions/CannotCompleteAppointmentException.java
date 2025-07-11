package fr.esgi.doctodocapi.model.appointment.exceptions;

import fr.esgi.doctodocapi.model.DomainException;

public class CannotCompleteAppointmentException extends DomainException {
    private static final String CODE = "appointment.cannot-complete";
    private static final String MESSAGE = "The appointment cannot be completed in the future.";

    public CannotCompleteAppointmentException() {
        super(CODE, MESSAGE);
    }
}
