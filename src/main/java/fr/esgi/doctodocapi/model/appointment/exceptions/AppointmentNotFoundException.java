package fr.esgi.doctodocapi.model.appointment.exceptions;

import fr.esgi.doctodocapi.model.DomainException;

public class AppointmentNotFoundException extends DomainException {
    private static final String CODE = "appointment.not-found";
    private static final String MESSAGE = "Le rendez-vous n'existe pas.";

    public AppointmentNotFoundException() {
        super(CODE, MESSAGE);
    }
}
