package fr.esgi.doctodocapi.model.care_tracking;

import fr.esgi.doctodocapi.model.DomainException;

public class AppointmentAlreadyExistInCareTrackingException extends DomainException {
    private static final String CODE = "appointment.already-exist";
    private static final String MESSAGE = "appointment already exist in this care tracking.";

    public AppointmentAlreadyExistInCareTrackingException() {
        super(CODE, MESSAGE);
    }
}