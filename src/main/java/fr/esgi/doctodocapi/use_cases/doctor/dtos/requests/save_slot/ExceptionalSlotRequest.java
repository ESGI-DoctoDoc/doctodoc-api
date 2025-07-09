package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_slot;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAlias;

public record ExceptionalSlotRequest(
        @JsonAlias("start") @NotNull LocalDate date,
        @NotNull LocalTime startHour,
        @NotNull LocalTime endHour,
        @NotNull List<UUID> medicalConcerns
) {}