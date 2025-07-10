package fr.esgi.doctodocapi.model.care_tracking;

import fr.esgi.doctodocapi.model.DomainException;

public class CareTrackingAlreadyExistException extends DomainException {
    private static final String CODE = "care-tracking.already-exist";
    private static final String MESSAGE = "Un dossier de suivi avec ce nom existe déjà pour ce patient.";

    public CareTrackingAlreadyExistException() {
        super(CODE, MESSAGE);
    }
}
