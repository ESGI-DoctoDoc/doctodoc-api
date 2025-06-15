package fr.esgi.doctodocapi.model.doctor.calendar;

import fr.esgi.doctodocapi.model.appointment.Appointment;
import fr.esgi.doctodocapi.model.doctor.calendar.absence.Absence;
import fr.esgi.doctodocapi.model.doctor.calendar.slot.Slot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Calendar {
    private final List<Appointment> appointments;
    private final List<Slot> slots;
    private final List<Absence> absences;

    public Calendar(List<Appointment> appointments, List<Slot> slots, List<Absence> absences) {
        this.appointments = new ArrayList<>(Objects.requireNonNullElse(appointments, List.of()));
        this.slots = new ArrayList<>(Objects.requireNonNullElse(slots, List.of()));
        this.absences = new ArrayList<>(Objects.requireNonNullElse(absences, List.of()));
    }

    public void addSlot(Slot slot) {
        if (slot == null) {
            throw new IllegalArgumentException("Cannot add a null slot to the calendar");
        }
        if (slots.contains(slot)) {
            throw new IllegalStateException("Duplicate slot already exists in the calendar: " + slot);
        }
        slots.add(slot);
    }

    public void removeSlot(Slot slot) {
        if (slot == null) {
            throw new IllegalArgumentException("Cannot remove a null slot from the calendar");
        }
        if (!slots.remove(slot)) {
            throw new IllegalStateException("Slot not found in the calendar: " + slot);
        }
    }

    public void addAbsence(Absence absence) {
        if (absence == null) {
            throw new IllegalArgumentException("Cannot add a null absence to the calendar");
        }
        if (absences.contains(absence)) {
            throw new IllegalStateException("Duplicate absence already exists in the calendar: " + absence);
        }
        absences.add(absence);
    }

    public void removeAbsence(Absence absence) {
        if (absence == null) {
            throw new IllegalArgumentException("Cannot remove a null absence from the calendar");
        }
        if (!absences.remove(absence)) {
            throw new IllegalStateException("Absence not found in the calendar: " + absence);
        }
    }

    public List<Slot> getSlots() {
        return Collections.unmodifiableList(slots);
    }

    public List<Appointment> getAppointments() {
        return Collections.unmodifiableList(appointments);
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments.clear();
        this.appointments.addAll(Objects.requireNonNullElse(appointments, List.of()));
    }

    public List<Absence> getAbsences() {
        return Collections.unmodifiableList(absences);
    }
}