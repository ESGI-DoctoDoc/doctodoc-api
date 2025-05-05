package fr.esgi.doctodocapi.model.patient;

import fr.esgi.doctodocapi.model.DomainException;

public class PatientMustHaveMajority extends DomainException {
    private static final String CODE = "birthdate.invalid";
    private static final String MESSAGE = "L'âge minimum doit être supérieur ou égal à 18 ans.";

    public PatientMustHaveMajority() {
        super(CODE, MESSAGE);
    }
}
