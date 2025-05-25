package fr.esgi.doctodocapi.model.appointment.exceptions;

import fr.esgi.doctodocapi.model.DomainException;

public class CannotBookAppointmentException extends DomainException {
    private static final String CODE = "appointment.conflict";
    private static final String MESSAGE = "Le rendez-vous ne peut pas être confirmé. Il y a un conflit.";

    public CannotBookAppointmentException() {
        super(CODE, MESSAGE);
    }
}
