package fr.esgi.doctodocapi.model.patient.medical_record;

import fr.esgi.doctodocapi.model.DomainException;

public class DocumentNotFoundInMedicalRecordException extends DomainException {
    private static final String CODE = "medical-record.document-not-found";
    private static final String MESSAGE = "Le document n'existe pas.";

    public DocumentNotFoundInMedicalRecordException() {
        super(CODE, MESSAGE);
    }
}
