package fr.esgi.doctodocapi.domain.entities.appointment.exceptions;

import fr.esgi.doctodocapi.domain.DomainException;

public class AppointmentStatusNotFound extends DomainException {
    private static final String CODE = "appointment-status.not-found";
    private static final String MESSAGE = "Le status de rendez-vous n'existe pas.";

    public AppointmentStatusNotFound() {
        super(CODE, MESSAGE);
    }
}
