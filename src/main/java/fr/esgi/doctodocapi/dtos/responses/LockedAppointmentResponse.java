package fr.esgi.doctodocapi.dtos.responses;

import java.util.UUID;

public record LockedAppointmentResponse(
        UUID appointmentLockedId
) {
}
