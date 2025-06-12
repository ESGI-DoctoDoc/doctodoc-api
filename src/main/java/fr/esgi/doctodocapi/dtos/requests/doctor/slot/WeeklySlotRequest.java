package fr.esgi.doctodocapi.dtos.requests.doctor.slot;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record WeeklySlotRequest(
        @NotNull DayOfWeek day,
        @NotNull LocalTime startHour,
        @NotNull LocalTime endHour,
        @NotNull LocalDate start,
        @NotNull LocalDate end,
        @NotNull List<UUID> medicalConcerns
) {}
