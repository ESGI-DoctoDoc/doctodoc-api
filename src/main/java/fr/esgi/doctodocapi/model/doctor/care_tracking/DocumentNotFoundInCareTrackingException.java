package fr.esgi.doctodocapi.model.doctor.care_tracking;

import fr.esgi.doctodocapi.model.DomainException;

public class DocumentNotFoundInCareTrackingException extends DomainException {
    private static final String CODE = "care-tracking.document-not-found";
    private static final String MESSAGE = "Le document n'existe pas.";

    public DocumentNotFoundInCareTrackingException() {
        super(CODE, MESSAGE);
    }
}
