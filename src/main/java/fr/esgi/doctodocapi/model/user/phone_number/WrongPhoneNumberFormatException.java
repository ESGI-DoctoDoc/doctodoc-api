package fr.esgi.doctodocapi.model.user.phone_number;

import fr.esgi.doctodocapi.model.DomainException;

public class WrongPhoneNumberFormatException extends DomainException {
    private static final String CODE = "phone-number.invalid";
    private static final String MESSAGE = "Le format du numéro de téléphone est invalide";

    public WrongPhoneNumberFormatException() {
        super(CODE, MESSAGE);
    }
}
