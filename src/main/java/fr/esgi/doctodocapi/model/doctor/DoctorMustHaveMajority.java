package fr.esgi.doctodocapi.model.doctor;

import fr.esgi.doctodocapi.model.DomainException;

public class DoctorMustHaveMajority extends DomainException {
    private static final String CODE = "birthdate.invalid";
    private static final String MESSAGE = "L'âge minimum doit être supérieur ou égal à 18 ans.";

    public DoctorMustHaveMajority() {
        super(CODE, MESSAGE);
    }
}
