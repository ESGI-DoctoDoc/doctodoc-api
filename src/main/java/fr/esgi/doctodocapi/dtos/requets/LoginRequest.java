package fr.esgi.doctodocapi.dtos.requets;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotBlank
        @NotNull
        String identifier,

        @NotBlank
        @NotNull
        String password
) {
}
