package fr.esgi.doctodocapi.dtos.requets;

import jakarta.validation.constraints.NotBlank;

public record LoginViaEmailRequest(
        @NotBlank
        String email,

        @NotBlank
        String password
) {
}
