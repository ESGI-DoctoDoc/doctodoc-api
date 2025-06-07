package fr.esgi.doctodocapi.dtos.requests.doctor.slot;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record SlotRequest(
        DayOfWeek day,
        @NotNull LocalTime startHour,
        @NotNull LocalTime endHour,
        @NotBlank String recurrence,
        LocalDate start,
        @NotNull LocalDate end,
        Integer dayNumber,
        @NotNull List<UUID> medicalConcerns
) {}
