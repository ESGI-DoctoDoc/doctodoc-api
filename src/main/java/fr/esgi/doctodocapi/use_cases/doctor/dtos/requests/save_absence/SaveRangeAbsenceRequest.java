package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_absence;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record SaveRangeAbsenceRequest(
        String description,
        @NotNull LocalDate start,
        @NotNull LocalDate end,
        @NotNull LocalTime startHour,
        @NotNull LocalTime endHour
) {
    public SaveRangeAbsenceRequest {
        if (description != null) {
            description = description.trim();
        }
    }
}