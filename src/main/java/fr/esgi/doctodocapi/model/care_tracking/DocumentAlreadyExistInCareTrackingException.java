package fr.esgi.doctodocapi.model.care_tracking;

import fr.esgi.doctodocapi.model.DomainException;

public class DocumentAlreadyExistInCareTrackingException extends DomainException {
    private static final String CODE = "document.already-exist";
    private static final String MESSAGE = "document already exist in this care tracking.";

    public DocumentAlreadyExistInCareTrackingException() {
        super(CODE, MESSAGE);
    }
}
