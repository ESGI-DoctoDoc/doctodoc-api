package fr.esgi.doctodocapi.use_cases.patient.dtos.responses.appointment_responses;

import java.util.UUID;

public record GetAppointmentDoctorResponse(
        UUID id,
        String firstName,
        String lastName,
        String speciality,
        String pictureUrl
) {
}
