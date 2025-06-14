package fr.esgi.doctodocapi.dtos.requests.doctor.absence;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SaveSingleDayAbsenceRequest(
        String description,
        @NotNull LocalDate date
) {
    public SaveSingleDayAbsenceRequest {
        if (description != null) {
            description = description.trim();
        }
    }
}