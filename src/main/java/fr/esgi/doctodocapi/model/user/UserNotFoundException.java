package fr.esgi.doctodocapi.model.user;

import fr.esgi.doctodocapi.model.DomainException;

public class UserNotFoundException extends DomainException {
    private static final String CODE = "user.not-found";
    private static final String MESSAGE = "L'utilisateur n'existe pas.";

    public UserNotFoundException() {
        super(CODE, MESSAGE);
    }
}
