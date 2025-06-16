package fr.esgi.doctodocapi.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ValidateDoubleAuthRequest(
        @NotNull
        @NotBlank
        @Size(min = 6, max = 6)
        String doubleAuthCode
) {
}
