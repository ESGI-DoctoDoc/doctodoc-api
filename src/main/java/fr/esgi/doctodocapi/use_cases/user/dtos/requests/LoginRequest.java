package fr.esgi.doctodocapi.use_cases.user.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotBlank
        @NotNull
        String identifier,

        @NotBlank
        @NotNull
        String password,

        @NotBlank
        @NotNull
        String verificationUrl
) {
}
