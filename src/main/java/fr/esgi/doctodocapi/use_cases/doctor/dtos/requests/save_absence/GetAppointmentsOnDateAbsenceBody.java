package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_absence;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record GetAppointmentsOnDateAbsenceBody(
        @NotNull LocalDate date
) {
}