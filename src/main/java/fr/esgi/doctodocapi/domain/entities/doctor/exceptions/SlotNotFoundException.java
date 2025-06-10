package fr.esgi.doctodocapi.domain.entities.doctor.exceptions;

import fr.esgi.doctodocapi.domain.DomainException;

public class SlotNotFoundException extends DomainException {
    private static final String CODE = "slot.not-found";
    private static final String MESSAGE = "Le slot n'existe pas.";

    public SlotNotFoundException() {
        super(CODE, MESSAGE);
    }
}
