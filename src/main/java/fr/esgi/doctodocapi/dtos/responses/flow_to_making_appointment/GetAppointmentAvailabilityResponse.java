package fr.esgi.doctodocapi.dtos.responses.flow_to_making_appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record GetAppointmentAvailabilityResponse(
        UUID slotId,
        LocalDate date,
        LocalTime start,
        LocalTime end,
        boolean isBooked
) {
}
