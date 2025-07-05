package fr.esgi.doctodocapi.use_cases.patient.dtos.responses.flow_to_making_appointment;

import java.util.UUID;

public record GetCareTrackingForAppointmentResponse(
        UUID id,
        String name,
        String description
) {
}
