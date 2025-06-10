package fr.esgi.doctodocapi.domain.entities.doctor.professionnal_informations.vo.rpps;

import fr.esgi.doctodocapi.domain.DomainException;

public class WrongRppsFormatException extends DomainException {
    private static final String CODE = "rpps.invalid";
    private static final String MESSAGE = "Le format du numéro rpps est invalide";

    public WrongRppsFormatException() {
        super(CODE, MESSAGE);
    }
}
