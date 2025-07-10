package fr.esgi.doctodocapi.use_cases.doctor.dtos.requests.save_slot;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record UpdateSlotRequest(
        @NotNull String startHour,
        @NotNull String endHour,
        @NotNull List<UUID> medicalConcernIds
) {}
