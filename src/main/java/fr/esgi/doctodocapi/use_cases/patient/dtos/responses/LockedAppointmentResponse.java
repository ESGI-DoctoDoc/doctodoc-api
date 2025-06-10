package fr.esgi.doctodocapi.use_cases.patient.dtos.responses;

import java.util.UUID;

public record LockedAppointmentResponse(
        UUID appointmentLockedId
) {
}
