package fr.esgi.doctodocapi.domain.entities.appointment.exceptions;

import fr.esgi.doctodocapi.domain.DomainException;

public class AppointmentNotFound extends DomainException {
    private static final String CODE = "appointment.not-found";
    private static final String MESSAGE = "Le rendez-vous n'existe pas.";

    public AppointmentNotFound() {
        super(CODE, MESSAGE);
    }
}
