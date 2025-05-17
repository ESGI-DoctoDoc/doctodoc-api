package fr.esgi.doctodocapi.model.appointment;

import fr.esgi.doctodocapi.model.DomainException;

public class AppointmentStatusNotFound extends DomainException {
    private static final String CODE = "appointment-status.not-found";
    private static final String MESSAGE = "Le status de rendez-vous n'existe pas.";

    public AppointmentStatusNotFound() {
        super(CODE, MESSAGE);
    }
}
