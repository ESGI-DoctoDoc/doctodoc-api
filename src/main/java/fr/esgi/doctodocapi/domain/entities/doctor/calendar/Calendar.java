package fr.esgi.doctodocapi.domain.entities.doctor.calendar;

import fr.esgi.doctodocapi.domain.entities.appointment.Appointment;

import java.util.List;

public class Calendar {
    private List<Appointment> appointments;
    private List<Slot> slots;

    public Calendar(List<Appointment> appointments, List<Slot> slots) {
        this.appointments = appointments;
        this.slots = slots;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }
}
