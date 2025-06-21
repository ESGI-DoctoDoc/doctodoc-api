package fr.esgi.doctodocapi.model.document;

import fr.esgi.doctodocapi.model.DomainException;

public class DocumentTypeNotFoundException extends DomainException {
    private static final String CODE = "document-type.not-found";
    private static final String MESSAGE = "Le type de document n'existe pas.";

    public DocumentTypeNotFoundException() {
        super(CODE, MESSAGE);
    }
}
