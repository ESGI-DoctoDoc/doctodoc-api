package fr.esgi.doctodocapi.dtos.responses.doctor;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OnboardingProcessResponse(
        @NotNull UUID doctorId
) {
}
