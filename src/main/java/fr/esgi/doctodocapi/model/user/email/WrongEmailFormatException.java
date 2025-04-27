package fr.esgi.doctodocapi.model.user.email;

import fr.esgi.doctodocapi.model.DomainException;

public final class WrongEmailFormatException extends DomainException {
    private static final String CODE = "email.invalid";
    private static final String MESSAGE = "Le format de l'email est invalide";

    public WrongEmailFormatException() {
        super(CODE, MESSAGE);
    }
}