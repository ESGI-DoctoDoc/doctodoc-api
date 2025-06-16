package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.slot_response;

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
