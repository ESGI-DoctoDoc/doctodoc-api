package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_absence;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record GetAppointmentsOnAbsenceBody(
        @NotNull LocalDate start,
        @NotNull LocalTime startHour,
        @NotNull LocalDate end,
        @NotNull LocalTime endHour
) {
}
