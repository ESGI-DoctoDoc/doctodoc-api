package fr.esgi.doctodocapi.dtos.requets;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginViaEmailRequest(
        @NotBlank
        @NotNull
        String email,

        @NotBlank
        @NotNull
        String password
) {
}
