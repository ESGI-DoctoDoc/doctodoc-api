package fr.esgi.doctodocapi.model.doctor.calendar;

import fr.esgi.doctodocapi.model.doctor.calendar.absence.Absence;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Calendar {
    private final List<Slot> slots;
    private final List<Absence> absences;

    public Calendar(List<Slot> slots, List<Absence> absences) {
        this.slots = new ArrayList<>(slots);
        this.absences = new ArrayList<>(absences);
    }

    public void addSlot(Slot slot) {
        if (slots.contains(slot)) {
            throw new IllegalStateException("Duplicate slot already exists in the calendar: " + slot);
        }
        slots.add(slot);
    }

    public void addAbsence(Absence absence) {
        if (absences.contains(absence)) {
            throw new IllegalStateException("Duplicate absence already exists in the calendar: " + absence);
        }
        absences.add(absence);
    }

    public List<Slot> getSlots() {
        return Collections.unmodifiableList(slots);
    }
    public List<Absence> getAbsences() {
        return Collections.unmodifiableList(absences);
    }
}