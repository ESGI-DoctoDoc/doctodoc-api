package fr.esgi.doctodocapi.model.doctor.care_tracking;

import fr.esgi.doctodocapi.model.DomainException;

public class CareTrackingNotFoundException extends DomainException {
    private static final String CODE = "care-tracking.not-found";
    private static final String MESSAGE = "Le suivi de dossier n'existe pas.";

    public CareTrackingNotFoundException() {
        super(CODE, MESSAGE);
    }
}