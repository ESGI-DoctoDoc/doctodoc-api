package fr.esgi.doctodocapi.model.medical_record;

import fr.esgi.doctodocapi.model.DomainException;

public class DocumentWithSameNameAlreadyExist extends DomainException {
    private static final String CODE = "medical-record.document-already-exist";
    private static final String MESSAGE = "Le document avec le même nom existe déjà.";

    public DocumentWithSameNameAlreadyExist() {
        super(CODE, MESSAGE);
    }
}
