package fr.esgi.doctodocapi.use_cases.patient.ports.in.make_appointment;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.save_appointment.SaveAppointmentRequest;
import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.LockedAppointmentResponse;

import java.util.UUID;

public interface IValidateAppointment {
    LockedAppointmentResponse lock(SaveAppointmentRequest saveAppointmentRequest);
    void unlocked(UUID id);
    void confirm(UUID id);
}
