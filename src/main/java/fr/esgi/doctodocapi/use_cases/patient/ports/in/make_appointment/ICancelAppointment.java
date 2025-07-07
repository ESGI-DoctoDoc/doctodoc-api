package fr.esgi.doctodocapi.use_cases.patient.ports.in.make_appointment;

import fr.esgi.doctodocapi.use_cases.patient.dtos.requests.CancelAppointmentRequest;

import java.util.UUID;

public interface ICancelAppointment {
    void cancel(UUID id, CancelAppointmentRequest cancelAppointmentRequest);
}
