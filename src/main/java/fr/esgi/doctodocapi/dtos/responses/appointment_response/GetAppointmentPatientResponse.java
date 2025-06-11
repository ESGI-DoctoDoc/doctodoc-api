package fr.esgi.doctodocapi.dtos.responses.appointment_response;

import java.util.UUID;

public record GetAppointmentPatientResponse(
        UUID id,
        String firstName,
        String lastName,
        String email
) {
}
