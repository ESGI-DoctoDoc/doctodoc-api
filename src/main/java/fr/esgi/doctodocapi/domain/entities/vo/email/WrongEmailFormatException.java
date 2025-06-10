package fr.esgi.doctodocapi.domain.entities.vo.email;

import fr.esgi.doctodocapi.domain.DomainException;

public final class WrongEmailFormatException extends DomainException {
    private static final String CODE = "email.invalid";
    private static final String MESSAGE = "Le format de l'email est invalide";

    public WrongEmailFormatException() {
        super(CODE, MESSAGE);
    }
}