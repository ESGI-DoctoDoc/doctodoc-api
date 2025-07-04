package fr.esgi.doctodocapi.model.medical_record;

import fr.esgi.doctodocapi.model.DomainException;

public class MedicalRecordNotFoundException extends DomainException {
    private static final String CODE = "medical-record.not-found";
    private static final String MESSAGE = "Le dossier médical n'existe pas.";

    public MedicalRecordNotFoundException() {
        super(CODE, MESSAGE);
    }
}
