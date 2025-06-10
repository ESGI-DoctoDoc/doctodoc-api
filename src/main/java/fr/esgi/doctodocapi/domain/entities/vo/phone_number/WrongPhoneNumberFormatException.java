package fr.esgi.doctodocapi.domain.entities.vo.phone_number;

import fr.esgi.doctodocapi.domain.DomainException;

public class WrongPhoneNumberFormatException extends DomainException {
    private static final String CODE = "phone-number.invalid";
    private static final String MESSAGE = "Le format du numéro de téléphone est invalide";

    public WrongPhoneNumberFormatException() {
        super(CODE, MESSAGE);
    }
}
