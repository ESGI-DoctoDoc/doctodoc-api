package fr.esgi.doctodocapi.model.doctor.calendar.slot;

import fr.esgi.doctodocapi.model.DomainException;

public class SlotCannotBeInThePastException extends DomainException {
    private static final String CODE = "slot.date.past";
    private static final String MESSAGE = "Impossible de créer un créneau dans le passé.";

    public SlotCannotBeInThePastException() {
        super(CODE, MESSAGE);
    }
}
