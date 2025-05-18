package fr.esgi.doctodocapi.model.appointment;

import fr.esgi.doctodocapi.model.doctor.exceptions.DoctorNotFoundException;
import fr.esgi.doctodocapi.model.doctor.exceptions.MedicalConcernNotFoundException;
import fr.esgi.doctodocapi.model.doctor.exceptions.SlotNotFoundException;
import fr.esgi.doctodocapi.model.patient.PatientNotFoundException;

import java.util.List;
import java.util.UUID;

public interface AppointmentRepository {
    Appointment getById(UUID id) throws AppointmentNotFound;
    List<Appointment> getAppointmentsBySlot(UUID slotId);

    void save(Appointment appointment) throws SlotNotFoundException, PatientNotFoundException, DoctorNotFoundException, MedicalConcernNotFoundException;
}
