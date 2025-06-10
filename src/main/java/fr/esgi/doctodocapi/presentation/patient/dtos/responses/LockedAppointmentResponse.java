package fr.esgi.doctodocapi.presentation.patient.dtos.responses;

import java.util.UUID;

public record LockedAppointmentResponse(
        UUID appointmentLockedId
) {
}
