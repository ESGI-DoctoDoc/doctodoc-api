package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.CompleteAppointmentResponse;

import java.util.UUID;

public interface ICompleteAppointment {
    CompleteAppointmentResponse process(UUID id);
}
