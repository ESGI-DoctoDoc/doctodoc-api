package fr.esgi.doctodocapi.model.doctor.calendar.slot;

import fr.esgi.doctodocapi.model.DomainException;

public class OverlappingSlotException extends DomainException {
    private static final String CODE = "slot.overlap";
    private static final String MESSAGE = "Cannot create slot: overlaps with an existing slot with the same medical concern";

    public OverlappingSlotException() {
        super(CODE, MESSAGE);
    }
}