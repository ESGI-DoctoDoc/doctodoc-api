package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses.absence_response;

import java.time.LocalDate;
import java.util.UUID;

public record GetAbsenceResponse(
        UUID id,
        String description,
        LocalDate date,
        LocalDate start,
        LocalDate end,
        String startHour,
        String endHour,
        LocalDate createdAt
) {
}
