package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_slot;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record ExceptionalSlotRequest(
        @NotNull LocalDate start,
        @NotNull LocalTime startHour,
        @NotNull LocalTime endHour,
        @NotNull List<UUID> medicalConcerns
) {
}