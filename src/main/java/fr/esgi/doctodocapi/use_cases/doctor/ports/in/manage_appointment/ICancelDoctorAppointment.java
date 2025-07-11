package fr.esgi.doctodocapi.use_cases.doctor.ports.in.manage_appointment;

import fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.appointment_response.GetCanceledAppointmentResponse;

import java.util.UUID;

public interface ICancelDoctorAppointment {
    GetCanceledAppointmentResponse cancel(UUID id, String reason);
}
