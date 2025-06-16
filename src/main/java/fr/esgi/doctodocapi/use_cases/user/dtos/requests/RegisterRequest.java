package fr.esgi.doctodocapi.use_cases.user.dtos.requests;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest (
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String phoneNumber
) {
}
