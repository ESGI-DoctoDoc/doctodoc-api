package fr.esgi.doctodocapi.model.document;

import fr.esgi.doctodocapi.model.DomainException;

public class DocumentNotFoundException extends DomainException {
    private static final String CODE = "document.not-found";
    private static final String MESSAGE = "Le document n'existe pas.";

    public DocumentNotFoundException() {
        super(CODE, MESSAGE);
    }
}
