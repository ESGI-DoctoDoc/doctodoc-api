package fr.esgi.doctodocapi.use_cases.patient.ports.in.make_appointment;

import fr.esgi.doctodocapi.use_cases.patient.dtos.responses.appointment_response.GetAppointmentDetailedResponse;

import java.util.UUID;

public interface IGetAppointmentDetail {
    GetAppointmentDetailedResponse get(UUID id);
}
