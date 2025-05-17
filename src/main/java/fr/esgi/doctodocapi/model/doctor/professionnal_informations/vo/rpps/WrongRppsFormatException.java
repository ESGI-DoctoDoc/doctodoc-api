package fr.esgi.doctodocapi.model.doctor.professionnal_informations.vo.rpps;

import fr.esgi.doctodocapi.model.DomainException;

public class WrongRppsFormatException extends DomainException {
    private static final String CODE = "rpps.invalid";
    private static final String MESSAGE = "Le format du numéro rpps est invalide";

    public WrongRppsFormatException() {
        super(CODE, MESSAGE);
    }
}
