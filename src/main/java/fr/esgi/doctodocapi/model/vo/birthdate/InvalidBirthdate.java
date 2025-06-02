package fr.esgi.doctodocapi.model.vo.birthdate;

import fr.esgi.doctodocapi.model.DomainException;

public class InvalidBirthdate extends DomainException {
    private static final String CODE = "birthdate.invalid";
    private static final String MESSAGE = "La date de naissance doit être inférieur à la date d'aujourd'hui";


    public InvalidBirthdate() {
        super(CODE, MESSAGE);
    }
}
