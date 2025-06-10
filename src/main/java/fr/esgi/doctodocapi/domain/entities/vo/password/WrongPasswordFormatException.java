package fr.esgi.doctodocapi.domain.entities.vo.password;

import fr.esgi.doctodocapi.domain.DomainException;

public class WrongPasswordFormatException extends DomainException {
    private static final String CODE = "password.invalid";
    private static final String MESSAGE = "Le format du mot de passe est invalide";

    public WrongPasswordFormatException() {
        super(CODE, MESSAGE);
    }
}
