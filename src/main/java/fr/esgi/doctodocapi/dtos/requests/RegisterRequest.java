package fr.esgi.doctodocapi.dtos.requests;

import jakarta.validation.constraints.*;

public record RegisterRequest (
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String phoneNumber
) {
}
