package fr.esgi.doctodocapi.model.doctor.professionnal_informations;

import fr.esgi.doctodocapi.model.DomainException;

public class DocumentAlreadyExistInDoctorException extends DomainException {
    private static final String CODE = "document.already-exist";
    private static final String MESSAGE = "document already exist in this doctor.";

    public DocumentAlreadyExistInDoctorException() {
        super(CODE, MESSAGE);
    }
}