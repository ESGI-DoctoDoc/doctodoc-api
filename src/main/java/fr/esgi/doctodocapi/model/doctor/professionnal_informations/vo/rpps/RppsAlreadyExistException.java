package fr.esgi.doctodocapi.model.doctor.professionnal_informations.vo.rpps;

import fr.esgi.doctodocapi.model.DomainException;

public class RppsAlreadyExistException extends DomainException {
    private static final String CODE = "rpps.already-exist";
    private static final String MESSAGE = "Le numéro rpps existe déjà";

    public RppsAlreadyExistException() {
        super(CODE, MESSAGE);
    }
}