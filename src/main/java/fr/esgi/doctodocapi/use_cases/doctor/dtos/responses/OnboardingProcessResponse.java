package fr.esgi.doctodocapi.use_cases.doctor.dtos.responses;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OnboardingProcessResponse(
        @NotNull UUID doctorId
) {
}
