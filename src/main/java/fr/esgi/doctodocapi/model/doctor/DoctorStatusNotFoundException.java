package fr.esgi.doctodocapi.model.doctor;

import fr.esgi.doctodocapi.model.DomainException;

public class DoctorStatusNotFoundException extends DomainException {
    private static final String CODE = "doctor-account-status.not-found";
    private static final String MESSAGE = "Le statut de compte n'existe pas.";

    public DoctorStatusNotFoundException() {
        super(CODE, MESSAGE);
    }
}