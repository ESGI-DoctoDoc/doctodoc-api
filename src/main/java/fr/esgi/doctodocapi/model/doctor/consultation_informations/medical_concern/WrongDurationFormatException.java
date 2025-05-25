package fr.esgi.doctodocapi.model.doctor.consultation_informations.medical_concern;

import fr.esgi.doctodocapi.model.DomainException;

public class WrongDurationFormatException extends DomainException {
    private static final String CODE = "duration.invalid";
    private static final String MESSAGE = "La durée doit être supérieure à 0.";

    public WrongDurationFormatException() {
        super(CODE, MESSAGE);
    }
}
