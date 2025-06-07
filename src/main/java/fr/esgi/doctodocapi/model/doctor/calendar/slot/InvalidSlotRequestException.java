package fr.esgi.doctodocapi.model.doctor.calendar.slot;

import fr.esgi.doctodocapi.model.DomainException;

public class InvalidSlotRequestException extends DomainException {
    private static final String CODE = "slot.invalid-request";
    private static final String MESSAGE = "La combinaison de champs dans la demande de slot est invalide.";

    public InvalidSlotRequestException() {
        super(CODE, MESSAGE);
    }
}
