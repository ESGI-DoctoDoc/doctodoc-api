package fr.esgi.doctodocapi.model.appointment;

import java.util.List;
import java.util.UUID;

public interface AppointmentRepository {
    List<Appointment> getAppointmentsBySlot(UUID slotId);
}
