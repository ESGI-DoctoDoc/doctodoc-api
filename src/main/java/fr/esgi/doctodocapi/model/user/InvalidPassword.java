package fr.esgi.doctodocapi.model.user;

import fr.esgi.doctodocapi.model.DomainException;

public class InvalidPassword extends DomainException {
    private static final String CODE = "account.bad-password";
    private static final String MESSAGE = "Password is invalid.";

    public InvalidPassword() {
        super(CODE, MESSAGE);
    }
}
