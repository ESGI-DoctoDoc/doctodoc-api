package fr.esgi.doctodocapi.dtos.responses.doctor.slot;

import java.util.UUID;

public record GetSlotResponse(
        UUID id,
        String date,
        String day,
        String startHour,
        String endHour,
        String recurrence,
        Integer dayNumber,
        UUID recurrenceId
) {
}
